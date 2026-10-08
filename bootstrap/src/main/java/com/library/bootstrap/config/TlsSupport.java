package com.library.bootstrap.config;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyPurposeId;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.eclipse.jetty.http.HttpCompliance;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.javalin.config.JavalinConfig;

/**
 * HTTPS support for the offline distribution (AppImage).
 *
 * <p>There is no CA and no internet in the target environment, so the certificate is a
 * per-machine self-signed one generated on first run with BouncyCastle and stored next to
 * the database in {@code ./data}. The key is unique per installation, so an AppImage can
 * be copied between machines and each copy keeps its own identity.</p>
 *
 * <p>Browsers warn once about the unknown issuer. For a self-hosted LAN application that
 * is the expected trade-off: over plain HTTP the credentials travel in clear text.</p>
 *
 * <p>The plain HTTP listener stays up as well, so nothing that works today breaks.</p>
 *
 * <p>Configuration (environment variables, all optional):
 * <ul>
 *   <li>{@code LIBRORA_TLS} — {@code false} disables HTTPS (HTTP-only mode).</li>
 *   <li>{@code LIBRORA_HTTPS_PORT} — HTTPS port, defaults to {@link AppConfig#HTTPS_PORT}.</li>
 *   <li>{@code LIBRORA_TLS_KEYSTORE} — keystore path, defaults to {@code ./data/librora-tls.p12}.</li>
 *   <li>{@code LIBRORA_TLS_PASSWORD} — keystore password, defaults to a generated one.</li>
 * </ul>
 */
public final class TlsSupport {

    private static final Logger LOG = LoggerFactory.getLogger(TlsSupport.class);

    private static final String ALIAS = "librora";
    private static final String KEYSTORE_TYPE = "PKCS12";
    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 2048;
    private static final int VALIDITY_DAYS = 3650;
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final String DEFAULT_KEYSTORE = "./data/librora-tls.p12";
    private static final String PASSWORD_FILE_NAME = "librora-tls.pass";

    private static volatile boolean active = false;
    private static volatile boolean disabled = false;

    private TlsSupport() {
    }

    /** Forces HTTPS off for this run (e.g. the port is already taken). */
    public static void disable() {
        disabled = true;
        active = false;
    }

    /** True once the HTTPS listener has been installed successfully. */
    public static boolean isActive() {
        return active;
    }

    /** True when the HTTPS listener should be started. */
    public static boolean isEnabled() {
        if (disabled) {
            return false;
        }
        String value = env("LIBRORA_TLS");
        if (value == null) {
            value = System.getProperty("librora.tls");
        }
        return value == null || !"false".equalsIgnoreCase(value.trim());
    }

    /** HTTPS port: {@code LIBRORA_HTTPS_PORT} or {@link AppConfig#HTTPS_PORT}. */
    public static int httpsPort() {
        String value = env("LIBRORA_HTTPS_PORT");
        if (value == null || value.isBlank()) {
            return AppConfig.HTTPS_PORT;
        }
        try {
            int port = Integer.parseInt(value.trim());
            return (port > 0 && port <= 65535) ? port : AppConfig.HTTPS_PORT;
        } catch (NumberFormatException e) {
            LOG.warn("Invalid LIBRORA_HTTPS_PORT='{}', falling back to {}", value, AppConfig.HTTPS_PORT);
            return AppConfig.HTTPS_PORT;
        }
    }

    /** Absolute HTTPS login URL for the given host. */
    public static String loginUrl(String host) {
        return "https://" + host + ":" + httpsPort() + "/login";
    }

    /**
     * Registers the HTTP and HTTPS listeners. Never throws: on any failure the app keeps
     * serving plain HTTP on {@link AppConfig#PORT} so the user is never left without a
     * server.
     *
     * <p>Javalin only creates its default HTTP connector when no connector has been
     * registered, so the HTTP one is registered explicitly here to keep both ports
     * available side by side.</p>
     */
    public static void install(JavalinConfig config, Logger log) {
        if (!isEnabled()) {
            log.info("HTTPS disabled via LIBRORA_TLS=false — serving HTTP only.");
            return;
        }
        active = false;
        try {
            Path keystorePath = Paths.get(valueOrDefault(env("LIBRORA_TLS_KEYSTORE"), DEFAULT_KEYSTORE));
            char[] password = resolvePassword(keystorePath);
            KeyStore keyStore = loadOrCreateKeyStore(keystorePath, password, log);
            int httpsPort = httpsPort();

            KeyStore finalKeyStore = keyStore;
            char[] finalPassword = password;

            config.jetty.addConnector((server, httpConfig) ->
                    createHttpConnector(server, httpConfig));
            config.jetty.addConnector((server, httpConfig) ->
                    createHttpsConnector(server, httpConfig, finalKeyStore, finalPassword, httpsPort));

            log.info("HTTPS enabled on port {} (self-signed certificate: {})",
                    httpsPort, keystorePath.toAbsolutePath());
            active = true;
        } catch (Exception e) {
            log.warn("Could not enable HTTPS ({}). Continuing with HTTP only on port {}.",
                    e.getMessage(), AppConfig.PORT);
        }
    }

    private static ServerConnector createHttpConnector(Server server, HttpConfiguration httpConfig) {
        ServerConnector connector = new ServerConnector(server, new HttpConnectionFactory(httpConfig));
        connector.setHost("0.0.0.0");
        connector.setPort(AppConfig.PORT);
        connector.setIdleTimeout(300_000);
        connector.setName("http-" + AppConfig.PORT);
        return connector;
    }

    private static ServerConnector createHttpsConnector(Server server,
                                                        HttpConfiguration baseConfig,
                                                        KeyStore keyStore,
                                                        char[] password,
                                                        int port) {
        SslContextFactory.Server sslContextFactory = new SslContextFactory.Server();
        sslContextFactory.setKeyStore(keyStore);
        sslContextFactory.setKeyStorePassword(new String(password));
        sslContextFactory.setKeyManagerPassword(new String(password));
        sslContextFactory.setWantClientAuth(false);
        sslContextFactory.setNeedClientAuth(false);

        HttpConfiguration httpsConfig = new HttpConfiguration(baseConfig);
        httpsConfig.setSecureScheme("https");
        httpsConfig.setSecurePort(port);
        httpsConfig.setHttpCompliance(HttpCompliance.RFC7230);
        // false = never answer with a redirect; both listeners must stay usable.
        httpsConfig.addCustomizer(new SecureRequestCustomizer(false));

        ServerConnector connector = new ServerConnector(
                server,
                new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()),
                new HttpConnectionFactory(httpsConfig));
        connector.setPort(port);
        connector.setHost("0.0.0.0");
        connector.setIdleTimeout(300_000);
        connector.setName("https-" + port);
        return connector;
    }

    private static KeyStore loadOrCreateKeyStore(Path path, char[] password, Logger log) throws Exception {
        if (Files.isRegularFile(path)) {
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
            try (InputStream in = Files.newInputStream(path)) {
                keyStore.load(in, password);
            }
            if (keyStore.getCertificate(ALIAS) != null) {
                log.info("Reusing existing TLS keystore: {}", path.toAbsolutePath());
                return keyStore;
            }
            log.warn("Keystore {} has no '{}' entry — regenerating.", path, ALIAS);
        }

        log.info("Generating a self-signed TLS certificate for this machine (first run)...");
        KeyStore keyStore = generate(password);

        Path parent = path.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (OutputStream out = Files.newOutputStream(path)) {
            keyStore.store(out, password);
        }
        restrictPermissions(path);
        log.info("TLS keystore written to {}", path.toAbsolutePath());
        return keyStore;
    }

    private static KeyStore generate(char[] password) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        generator.initialize(KEY_SIZE, SecureRandom.getInstanceStrong());
        KeyPair keyPair = generator.generateKeyPair();

        X500Name subject = new X500Name("CN=librora.local,OU=Librora,O=Librora,C=ES");
        Instant now = Instant.now();

        JcaX509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                subject,
                BigInteger.valueOf(now.toEpochMilli()),
                Date.from(now.minus(1, ChronoUnit.DAYS)),
                Date.from(now.plus(VALIDITY_DAYS, ChronoUnit.DAYS)),
                subject,
                keyPair.getPublic());

        builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(false));
        builder.addExtension(Extension.keyUsage, true, new KeyUsage(
                KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.keyCertSign));
        builder.addExtension(Extension.extendedKeyUsage, false,
                new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));
        builder.addExtension(Extension.subjectAlternativeName, false, subjectAltNames());
        builder.addExtension(Extension.subjectKeyIdentifier, false,
                new JcaX509ExtensionUtils().createSubjectKeyIdentifier(keyPair.getPublic()));

        ContentSigner signer = new JcaContentSignerBuilder(SIGNATURE_ALGORITHM).build(keyPair.getPrivate());
        X509Certificate certificate = new JcaX509CertificateConverter().getCertificate(builder.build(signer));
        certificate.checkValidity();

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);
        keyStore.load(null, password);
        keyStore.setKeyEntry(ALIAS, keyPair.getPrivate(), password, new Certificate[]{certificate});
        return keyStore;
    }

    private static GeneralNames subjectAltNames() {
        Set<String> dnsNames = new LinkedHashSet<>();
        Set<String> ipAddresses = new LinkedHashSet<>();

        dnsNames.add("localhost");
        dnsNames.add("librora.local");
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            if (hostname != null && !hostname.isBlank()) {
                dnsNames.add(hostname);
            }
        } catch (Exception e) {
            LOG.debug("Could not resolve local hostname: {}", e.getMessage());
        }

        ipAddresses.add("127.0.0.1");
        ipAddresses.add("::1");
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces != null && interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    String host = addresses.nextElement().getHostAddress();
                    if (host != null && !host.isBlank()) {
                        ipAddresses.add(host);
                    }
                }
            }
        } catch (Exception e) {
            LOG.debug("Could not enumerate local addresses: {}", e.getMessage());
        }

        List<GeneralName> names = new ArrayList<>();
        for (String dns : dnsNames) {
            names.add(new GeneralName(GeneralName.dNSName, dns));
        }
        for (String ip : ipAddresses) {
            String normalized = ip.startsWith("/") ? ip.substring(1) : ip;
            try {
                names.add(new GeneralName(GeneralName.iPAddress, normalized));
            } catch (Exception e) {
                LOG.debug("Skipping address {}: {}", normalized, e.getMessage());
            }
        }
        return new GeneralNames(names.toArray(new GeneralName[0]));
    }

    private static char[] resolvePassword(Path keystorePath) throws Exception {
        String fromEnv = env("LIBRORA_TLS_PASSWORD");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv.toCharArray();
        }

        Path parent = keystorePath.toAbsolutePath().getParent();
        Path passwordFile = (parent == null ? Paths.get(".") : parent).resolve(PASSWORD_FILE_NAME);

        if (Files.isRegularFile(passwordFile)) {
            String stored = Files.readString(passwordFile, StandardCharsets.UTF_8).trim();
            if (!stored.isEmpty()) {
                return stored.toCharArray();
            }
        }

        byte[] raw = new byte[24];
        SecureRandom.getInstanceStrong().nextBytes(raw);
        String password = Base64.getUrlEncoder().withoutPadding().encodeToString(raw);

        Files.createDirectories(passwordFile.toAbsolutePath().getParent());
        Files.writeString(passwordFile, password, StandardCharsets.UTF_8);
        restrictPermissions(passwordFile);
        return password.toCharArray();
    }

    private static void restrictPermissions(Path path) {
        try {
            java.io.File file = path.toFile();
            if (!file.setReadable(false, false)) {
                return;
            }
            file.setReadable(true, true);
            file.setWritable(true, true);
        } catch (Exception e) {
            LOG.debug("Could not restrict permissions on {}: {}", path, e.getMessage());
        }
    }

    private static String env(String name) {
        String value = System.getenv(name);
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    private static String valueOrDefault(String value, String fallback) {
        return (value == null || value.isBlank()) ? fallback : value.trim();
    }
}
