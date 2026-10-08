package com.library.bootstrap;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import javax.swing.*;
import javax.swing.border.Border;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.bootstrap.config.AppConfig;
import com.library.bootstrap.config.FlywayMigrations;
import com.library.bootstrap.config.JavalinStart;
import com.library.bootstrap.config.TlsSupport;
import com.library.bootstrap.config.hibernate.HibernateConfiguration;
import com.library.bootstrap.factory.IamFactory;
import com.library.bootstrap.scheduler.MembershipExpiryScheduler;
import com.library.bootstrap.upload.ImageStorage;
import com.library.iam.domain.port.out.UserPort;
import com.library.iam.infrastructure.security.BouncyCastleArgon2PasswordHasher;

public class ServerWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ServerWindow.class);

    private JLabel statusLabel;
    private JLabel ipLabel;
    private JLabel loadingLabel;
    private Timer loadingTimer;
    private JPanel loadingPanel;
    private JPanel readyPanel;
    private JPanel errorPanel;
    private JProgressBar progressBar;
    private JButton openButton;
    private JButton copyButton;
    private JButton restartButton;
    private SwingWorker<Void, String> currentWorker;
    private static JmDNS jmdns;
    private static Process avahiProcess;

    private static final int CONTENT_WIDTH = 320;
    private static final int LOGO_SIZE = 88;

    public ServerWindow() {
        super("Librora");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(loadIcon());
        initUI();
        // Height from pack() (the content decides it), width pinned to the content
        // column plus the real window insets so nothing can stretch or clip.
        pack();
        Insets insets = getInsets();
        setSize(CONTENT_WIDTH + 56 + insets.left + insets.right, getHeight());
        setResizable(false);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        animateLoadingDots();
        startServer();
    }

    private java.awt.Image loadIcon() {
        java.net.URL url = getClass().getResource("/static/img/librora-logo.png");
        if (url == null) return null;
        try {
            return scaleToFit(javax.imageio.ImageIO.read(url), LOGO_SIZE);
        } catch (Exception e) {
            log.debug("Could not read logo resource: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Scales an image into a square of {@code size} px.
     *
     * <p>Required: the raw logo is 1448x1086. A plain {@code JLabel} renders it at
     * its natural size, so inside a vertical BoxLayout it grows to 1086px tall and
     * pushes every other component below the bottom of the window — which is what
     * made the launcher look empty.</p>
     */
    private static java.awt.Image scaleToFit(java.awt.Image source, int size) {
        if (source == null) return null;
        int w = source.getWidth(null);
        int h = source.getHeight(null);
        if (w <= 0 || h <= 0) return source;

        double scale = Math.min((double) size / w, (double) size / h);
        int tw = Math.max(1, (int) Math.round(w * scale));
        int th = Math.max(1, (int) Math.round(h * scale));

        java.awt.image.BufferedImage out =
                new java.awt.image.BufferedImage(tw, th, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g = out.createGraphics();
        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,
                java.awt.RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(source, 0, 0, tw, th, null);
        g.dispose();
        return out;
    }

    /** Centred, width-capped column so no label can exceed the window. */
    private static JPanel column() {
        JPanel panel = new JPanel();
        panel.setBackground(new java.awt.Color(0x0b0d11));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(28, 28, 28, 28));
        // No explicit preferredSize: BoxLayout must keep computing the real height
        // as components are added, otherwise pack() freezes the window too short
        // and the content is clipped.
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return panel;
    }

    /** A label that stays inside {@link #CONTENT_WIDTH} and wraps instead of clipping. */
    private static JLabel fixedLabel(String text, int fontSize, int style, java.awt.Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", style, fontSize));
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setMaximumSize(new Dimension(CONTENT_WIDTH, Integer.MAX_VALUE));
        return label;
    }

    private static JButton fixedButton(String text, java.awt.Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(background);
        button.setForeground(new java.awt.Color(0xffffff));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 40));
        button.setPreferredSize(new Dimension(220, 40));
        return button;
    }

    private void animateLoadingDots() {
        if (loadingLabel == null) return;
        loadingTimer = new Timer(500, e -> {
            String text = loadingLabel.getText();
            int dots = (text.length() - "Cargando".length());
            dots = (dots + 1) % 4;
            StringBuilder sb = new StringBuilder("Cargando");
            for (int i = 0; i < dots; i++) sb.append(".");
            loadingLabel.setText(sb.toString());
        });
        loadingTimer.start();
    }

    private void stopLoadingAnimation() {
        if (loadingTimer != null && loadingTimer.isRunning()) {
            loadingTimer.stop();
        }
        if (loadingLabel != null) {
            loadingLabel.setText("Cargando");
        }
    }

    private void initUI() {
        getContentPane().setBackground(new java.awt.Color(0x0b0d11));
        setLayout(new CardLayout());

        loadingPanel = createLoadingPanel();
        readyPanel = createReadyPanel();
        errorPanel = createErrorPanel();

        add(loadingPanel, "LOADING");
        add(readyPanel, "READY");
        add(errorPanel, "ERROR");

        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "LOADING");
    }

    private JPanel createLoadingPanel() {
        JPanel panel = column();

        java.awt.Image icon = loadIcon();
        if (icon != null) {
            JLabel iconLabel = new JLabel(new ImageIcon(icon));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            iconLabel.setPreferredSize(new Dimension(LOGO_SIZE, LOGO_SIZE));
            iconLabel.setMinimumSize(new Dimension(LOGO_SIZE, LOGO_SIZE));
            iconLabel.setMaximumSize(new Dimension(LOGO_SIZE, LOGO_SIZE));
            panel.add(iconLabel);
            panel.add(Box.createVerticalStrut(18));
        }

        panel.add(fixedLabel("Iniciando Librora", 22, Font.BOLD, new java.awt.Color(0xf1f2f6)));
        panel.add(Box.createVerticalStrut(6));
        panel.add(fixedLabel("Preparando el sistema, por favor espera...", 13, Font.PLAIN,
                new java.awt.Color(0x8b8f98)));
        panel.add(Box.createVerticalStrut(20));

        // Determinate bar driven by the startup steps, so the window always shows
        // forward progress instead of an endless spinner.
        progressBar = new JProgressBar(0, STARTUP_STEPS.length);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(CONTENT_WIDTH, 8));
        progressBar.setMaximumSize(new Dimension(CONTENT_WIDTH, 8));
        progressBar.setForeground(new java.awt.Color(0x4a9eff));
        progressBar.setBackground(new java.awt.Color(0x2a2d35));
        progressBar.setBorderPainted(false);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(progressBar);
        panel.add(Box.createVerticalStrut(12));

        loadingLabel = new JLabel("Cargando");
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loadingLabel.setForeground(new java.awt.Color(0x4a9eff));
        loadingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadingLabel.setMaximumSize(new Dimension(CONTENT_WIDTH, 20));
        panel.add(loadingLabel);
        panel.add(Box.createVerticalStrut(6));

        statusLabel = fixedLabel("", 11, Font.PLAIN, new java.awt.Color(0x6c7080));
        panel.add(statusLabel);

        return panel;
    }

    private JPanel createReadyPanel() {
        JPanel panel = column();

        panel.add(fixedLabel("\u2713", 40, Font.BOLD, new java.awt.Color(0x22c55e)));
        panel.add(Box.createVerticalStrut(8));
        panel.add(fixedLabel("Servidor activo", 20, Font.BOLD, new java.awt.Color(0xf1f2f6)));
        panel.add(Box.createVerticalStrut(12));

        ipLabel = new JLabel("");
        ipLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
        ipLabel.setForeground(new java.awt.Color(0x4a9eff));
        ipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ipLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ipLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ipLabel.setPreferredSize(new Dimension(CONTENT_WIDTH, 22));
        ipLabel.setMaximumSize(new Dimension(CONTENT_WIDTH, 40));
        ipLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                copyToClipboard(ipLabel.getText());
            }
        });
        panel.add(ipLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(fixedLabel("Haz clic para copiar", 11, Font.ITALIC, new java.awt.Color(0x6c7080)));
        panel.add(Box.createVerticalStrut(14));

        java.awt.Color muted = new java.awt.Color(0x8b8f98);
        panel.add(fixedLabel("Desde otros dispositivos en la misma red:", 12, Font.PLAIN, muted));
        panel.add(fixedLabel("https://librora.local:" + TlsSupport.httpsPort(), 12, Font.PLAIN, muted));
        panel.add(fixedLabel("o https://TU_IP:" + TlsSupport.httpsPort(), 12, Font.PLAIN, muted));
        panel.add(fixedLabel("sin TLS: http://librora.local:" + AppConfig.PORT, 11, Font.PLAIN, muted));
        panel.add(Box.createVerticalStrut(16));

        openButton = fixedButton("Abrir navegador", new java.awt.Color(0x4a9eff));
        openButton.addActionListener(e -> openBrowser(ipLabel.getText()));
        panel.add(openButton);
        panel.add(Box.createVerticalStrut(8));

        copyButton = new JButton("Copiar direccion");
        copyButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        copyButton.setBackground(new java.awt.Color(0x2a2d35));
        copyButton.setForeground(new java.awt.Color(0xf1f2f6));
        copyButton.setFocusPainted(false);
        copyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        copyButton.setMargin(new Insets(6, 20, 6, 20));
        copyButton.setPreferredSize(new Dimension(220, 34));
        copyButton.setMaximumSize(new Dimension(220, 34));
        copyButton.addActionListener(e -> copyToClipboard(ipLabel.getText()));
        panel.add(copyButton);

        return panel;
    }

    private JPanel createErrorPanel() {
        JPanel panel = column();

        panel.add(fixedLabel("\u2717", 40, Font.BOLD, new java.awt.Color(0xef4444)));
        panel.add(Box.createVerticalStrut(12));
        panel.add(fixedLabel("Error al iniciar", 20, Font.BOLD, new java.awt.Color(0xef4444)));
        panel.add(Box.createVerticalStrut(12));
        java.awt.Color muted = new java.awt.Color(0x8b8f98);
        panel.add(fixedLabel("Asegurate de que el puerto " + AppConfig.PORT, 13, Font.PLAIN, muted));
        panel.add(fixedLabel("este libre y de que la carpeta data/", 13, Font.PLAIN, muted));
        panel.add(fixedLabel("sea escribible.", 13, Font.PLAIN, muted));
        panel.add(Box.createVerticalStrut(20));

        restartButton = fixedButton("Reintentar", new java.awt.Color(0x4a9eff));
        restartButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) getContentPane().getLayout();
            cl.show(getContentPane(), "LOADING");
            statusLabel.setText("");
            startServer();
        });
        panel.add(restartButton);

        return panel;
    }


    private void startServer() {
        startupStep = 0;
        if (progressBar != null) {
            progressBar.setValue(0);
        }
        currentWorker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    step("Verificando puertos..."); publish("Verificando puerto " + AppConfig.PORT + "...");
                    if (!tryBindPort(AppConfig.PORT)) {
                        throw new RuntimeException("El puerto " + AppConfig.PORT + " ya está en uso. Cierra otra instancia de Librora.");
                    }
                    if (TlsSupport.isEnabled() && !tryBindPort(TlsSupport.httpsPort())) {
                        publish("El puerto HTTPS " + TlsSupport.httpsPort() + " está ocupado...");
                        log.warn("HTTPS port {} is already in use — continuing with HTTP only.", TlsSupport.httpsPort());
                        TlsSupport.disable();
                    }

                    step("Preparando almacenamiento de imagenes...");
                    ImageStorage.init();

                    java.io.File dbFile = new java.io.File("./data/library.mv.db");
                    if (dbFile.exists()) {
                        step("Abriendo la base de datos...");
                    } else {
                        step("Creando la base de datos...");
                        FlywayMigrations.run(log);
                    }

                    step("Iniciando motor de base de datos...");
                    final SessionFactory sessionFactory = HibernateConfiguration.buildHibernateConfiguration().buildSessionFactory();

                    step("Preparando el usuario administrador...");
                    seedDefaultPasswords(sessionFactory);

                    step("Iniciando el servidor web...");
                    JavalinStart.run(sessionFactory, log);

                    step("Anunciando en la red local...");
                    startMdnsAdvertisement(AppConfig.PORT);

                    step("Iniciando tareas en segundo plano...");
                    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
                        Thread t = new Thread(r, "membership-expiry-scheduler");
                        t.setDaemon(true);
                        return t;
                    });
                    scheduler.scheduleAtFixedRate(
                            () -> MembershipExpiryScheduler.run(sessionFactory),
                            1, 60, TimeUnit.MINUTES);

                    Thread keepAlive = new Thread(() -> {
                        try {
                            while (!Thread.currentThread().isInterrupted()) {
                                Thread.sleep(60000);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }, "server-keepalive");
                    keepAlive.setDaemon(false);
                    keepAlive.start();

                    step("Finalizando el arranque...");

                    String url = buildAccessUrl();
                    log.info("Opening {}", url);

                    SwingUtilities.invokeLater(() -> {
                        ipLabel.setText(url);
                        CardLayout cl = (CardLayout) getContentPane().getLayout();
                        stopLoadingAnimation();
                        cl.show(getContentPane(), "READY");
                        revalidate();
                        repaint();
                    });

                    if (!GraphicsEnvironment.isHeadless()) {
                        Thread browserThread = new Thread(() -> openBrowser(url), "browser-opener");
                        browserThread.setDaemon(true);
                        browserThread.start();
                    } else {
                        log.info("Headless mode - open manually: {}", url);
                    }

                    return null;
                } catch (Exception e) {
                    log.error("Failed to start server", e);
                    String errorMsg = e.getMessage() != null ? e.getMessage() : "Error desconocido";
                    SwingUtilities.invokeLater(() -> showError(errorMsg));
                    return null;
                }
            }

            @Override
            protected void process(List<String> chunks) {
                statusLabel.setText(chunks.get(chunks.size() - 1));
            }
        };
        currentWorker.execute();
    }

    /**
     * Startup steps, in order. The progress bar is determinate over this list so
     * the launcher always shows real forward progress.
     */
    private static final String[] STARTUP_STEPS = {
            "Verificando puertos...",
            "Preparando almacenamiento de imagenes...",
            "Abriendo la base de datos...",
            "Iniciando motor de base de datos...",
            "Preparando el usuario administrador...",
            "Iniciando el servidor web...",
            "Anunciando en la red local...",
            "Iniciando tareas en segundo plano...",
            "Finalizando el arranque...",
    };

    private static int startupStep = 0;

    private void step(String message) {
        startupStep++;
        int value = Math.min(startupStep, STARTUP_STEPS.length);
        if (progressBar != null) {
            progressBar.setValue(value);
            progressBar.setStringPainted(false);
        }
        log.info("[{}/{}] {}", value, STARTUP_STEPS.length, message);
    }

    /**
     * URL opened in the browser and shown in the window.
     *
     * <p>Prefers {@code https://librora.local:8443} so the certificate SAN matches
     * and the same address works from any device on the network. Falls back to the
     * LAN IP when mDNS/hosts setup did not resolve librora.local on this machine,
     * and to plain HTTP when TLS is off or the port was busy.</p>
     */
    private String buildAccessUrl() {
        if (!TlsSupport.isActive()) {
            String host = resolveOrLan("librora.local");
            return "http://" + host + ":" + AppConfig.PORT + "/login";
        }
        return TlsSupport.loginUrl(resolveOrLan("librora.local"));
    }

    /** Returns {@code name} if it resolves here, otherwise the LAN address. */
    private static String resolveOrLan(String name) {
        try {
            InetAddress.getByName(name);
            return name;
        } catch (Exception e) {
            String lan = getLanAddress();
            return lan != null ? lan : name;
        }
    }

    private void showError(String message) {
        stopLoadingAnimation();
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), "ERROR");
        pack();
        setLocationRelativeTo(null);
    }

    private static String getLanAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual()) continue;
                Enumeration<java.net.InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    java.net.InetAddress addr = addresses.nextElement();
                    if (addr instanceof java.net.Inet4Address && !addr.isLoopbackAddress()) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            // fallback below
        }
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean tryBindPort(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            socket.setReuseAddress(true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void startMdnsAdvertisement(int port) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("linux")) {
            if (System.getenv("APPIMAGE") != null) {
                // Inside the AppImage the AppRun script already attempted the
                // full avahi (sudo) sequence. AppRun exported LIBRORA_DNS:
                //   "avahi" -> avahi set the host name + published the service.
                //   "jmix"  -> sudo unavailable; advertise librora.local via JmDNS (no root).
                //   "hosts" -> avahi unavailable; advertise via JmDNS (no root).
                String dns = System.getenv("LIBRORA_DNS");
                if ("avahi".equals(dns)) {
                    // AppRun already published both _http._tcp and _https._tcp.
                    log.info("Running inside AppImage: mDNS handled by AppRun (avahi).");
                    registerShutdown();
                    return;
                }
                log.info("Running inside AppImage: avahi/sudo unavailable; advertising librora.local via JmDNS (sudo-free).");
                startMdnsJmdns(port);
                advertiseHttpsService(jmdns);
                // Offer the interactive privileged avahi setup in a terminal so the
                // user can grant sudo once and make librora.local permanent/network-wide.
                if ("1".equals(System.getenv("LIBRORA_PRIV_SETUP"))) {
                    offerPrivilegedTerminalSetup();
                }
                return;
            }
            startMdnsAvahi(port);
            advertiseHttpsService(null);
        } else {
            startMdnsJmdns(port);
            advertiseHttpsService(jmdns);
        }
    }

    /**
     * Publishes the HTTPS listener as {@code _https._tcp} so other devices can discover
     * {@code https://librora.local:8443} and not only the plain HTTP port.
     *
     * @param jm the running JmDNS instance, or {@code null} to publish through avahi
     */
    private static void advertiseHttpsService(javax.jmdns.JmDNS jm) {
        if (!TlsSupport.isActive()) {
            return;
        }
        int httpsPort = TlsSupport.httpsPort();
        try {
            if (jm != null) {
                jm.registerService(javax.jmdns.ServiceInfo.create(
                        "_https._tcp.local.", "Librora", httpsPort, "/"));
            } else {
                ProcessBuilder pb = new ProcessBuilder(
                        "avahi-publish-service", "-s", "Librora", "_https._tcp",
                        String.valueOf(httpsPort), "path=/");
                pb.redirectErrorStream(true);
                pb.redirectInput(ProcessBuilder.Redirect.from(new java.io.File("/dev/null")));
                Process httpsProcess = pb.start();
                Runtime.getRuntime().addShutdownHook(new Thread(httpsProcess::destroy));
            }
            log.info("HTTPS service published on port {} — reachable at https://librora.local:{}", httpsPort, httpsPort);
        } catch (Exception e) {
            log.warn("Could not publish the HTTPS mDNS service: {}", e.getMessage());
        }
    }

    private static boolean privSetupOffered = false;

    private static void offerPrivilegedTerminalSetup() {
        if (privSetupOffered) return;
        privSetupOffered = true;
        try {
            String appdir = System.getenv("APPDIR");
            if (appdir == null || appdir.isEmpty()) return;
            java.io.File script = new java.io.File(appdir, "librora-avahi-setup.sh");
            if (!script.exists()) return;

            // (terminal, arg-form, fixed-args...) - try the available emulator.
            String[][] candidates = {
                    {"x-terminal-emulator", "-e"},
                    {"xterm", "-e"},
                    {"mate-terminal", "-e"},
                    {"konsole", "-e"},
                    {"gnome-terminal", "--"},
                    {"xfce4-terminal", "-e"},
                    {"lxterminal", "-e"},
            };
            for (String[] c : candidates) {
                String term = c[0];
                if (new ProcessBuilder("which", term).start().waitFor() != 0) continue;
                ProcessBuilder pb;
                if (c[1].equals("--")) {
                    pb = new ProcessBuilder(term, "--", "bash", script.getAbsolutePath());
                } else {
                    String cmd = "bash " + script.getAbsolutePath();
                    pb = new ProcessBuilder(term, "-e", cmd);
                }
                pb.redirectErrorStream(true);
                pb.redirectOutput(ProcessBuilder.Redirect.DISCARD);
                Process p = pb.start();
                log.info("Opened terminal to run '{}' (granted sudo will persist librora.local network-wide).", script.getName());
                Thread monitor = new Thread(() -> {
                    try { p.waitFor(); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                }, "priv-setup-terminal");
                monitor.setDaemon(true);
                monitor.start();
                return;
            }
            log.info("No terminal emulator found; run manually: sudo bash {}/librora-avahi-setup.sh", appdir);
        } catch (Exception e) {
            log.debug("Could not launch privileged setup terminal: {}", e.getMessage());
        }
    }



    private static void registerShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
                log.info("Librora shutdown received")));
    }

    private static void startMdnsAvahi(int port) {
        try {
            Process which = Runtime.getRuntime().exec(new String[]{"which", "avahi-publish-service"});
            int exit = which.waitFor();
            if (exit != 0) {
                log.warn("avahi not found, mDNS unavailable on Linux. librora.local will not resolve on other devices.");
                return;
            }
            if (!isAvahiRunning()) {
                log.info("avahi-daemon not running, starting it...");
                boolean started = runSudo(new String[]{"systemctl", "start", "avahi-daemon"});
                if (!started) {
                    log.warn("Could not start avahi-daemon. Run manually: sudo systemctl start avahi-daemon");
                }
                Thread.sleep(2000);
            }
            boolean hostSet = runSudo(new String[]{"avahi-set-host-name", "librora"});
            if (hostSet) {
                log.info("Hostname set to 'librora' — librora.local will resolve via mDNS on all devices");
            } else {
                log.warn("Could not set hostname. Run manually: sudo avahi-set-host-name librora");
            }
            ProcessBuilder pb = new ProcessBuilder(
                    "avahi-publish-service",
                    "-s",
                    "Librora",
                    "_http._tcp",
                    String.valueOf(port),
                    "path=/"
            );
            pb.redirectErrorStream(true);
            avahiProcess = pb.start();
            log.info("mDNS via avahi started — Librora service published.");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (avahiProcess != null && avahiProcess.isAlive()) {
                    avahiProcess.destroy();
                    log.info("avahi mDNS stopped");
                }
            }));
        } catch (Exception e) {
            log.warn("Could not start avahi mDNS: {}", e.getMessage());
        }
    }

    private static boolean runSudo(String... args) {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            java.util.List<String> cmd = new java.util.ArrayList<>();
            cmd.add("sudo");
            cmd.add("-n");
            for (String a : args) cmd.add(a);
            pb.command(cmd);
            pb.redirectErrorStream(true);
            pb.redirectInput(ProcessBuilder.Redirect.from(new java.io.File("/dev/null")));
            Process sudo = pb.start();
            return sudo.waitFor() == 0;
        } catch (Exception e) {
            log.debug("sudo -n failed: {}", e.getMessage());
            return false;
        }
    }

    private static boolean isAvahiRunning() {
        try {
            Process ps = Runtime.getRuntime().exec(new String[]{"pgrep", "-x", "avahi-daemon"});
            return ps.waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static void startMdnsJmdns(int port) {
        try {
            String lanAddr = getLanAddress();
            if (lanAddr == null) {
                lanAddr = InetAddress.getLocalHost().getHostAddress();
            }
            jmdns = JmDNS.create(InetAddress.getByName(lanAddr), "librora");
            ServiceInfo serviceInfo = ServiceInfo.create("_http._tcp.local.", "Librora", port, "/");
            jmdns.registerService(serviceInfo);
            log.info("mDNS advertisement started — Librora is reachable at http://librora.local:{} on this network", port);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (jmdns != null) {
                    try {
                        jmdns.unregisterAllServices();
                        jmdns.close();
                        log.info("mDNS shut down cleanly");
                    } catch (Exception e) {
                        log.warn("Error shutting down mDNS: {}", e.getMessage());
                    }
                }
                if (avahiProcess != null && avahiProcess.isAlive()) {
                    avahiProcess.destroy();
                    log.info("avahi mDNS process stopped");
                }
            }));
        } catch (IOException e) {
            log.warn("Could not start mDNS advertisement: {}", e.getMessage());
        } catch (Exception e) {
            log.warn("Could not start mDNS advertisement: {}", e.getMessage(), e);
        }
    }

    /**
     * Single-superuser bootstrap: makes the seeded {@code admin} account usable on
     * the very first launch.
     *
     * <p>Only applied while the stored hash is still the placeholder written by the
     * Flyway seed. Resetting unconditionally would silently revert the owner's chosen
     * password to {@code admin123} on every restart.</p>
     */
    private static void seedDefaultPasswords(SessionFactory sessionFactory) {
        String username = System.getenv().getOrDefault("LIBRORA_ADMIN_USER", "admin");
        String defaultPassword = System.getenv().getOrDefault("LIBRORA_ADMIN_PASSWORD", "admin123");
        try {
            UserPort userPort = IamFactory.userPort(sessionFactory);
            BouncyCastleArgon2PasswordHasher hasher = new BouncyCastleArgon2PasswordHasher();
            userPort.findByUsername(username).ifPresentOrElse(user -> {
                String current = user.getPassword();
                if (isPlaceholderHash(current)) {
                    userPort.updatePassword(username, hasher.hash(defaultPassword));
                    log.warn("Initial password set for user '{}'. Change it from the "
                            + "Users screen on first login.", username);
                } else {
                    log.info("User '{}' already has a custom password — left untouched.", username);
                }
            }, () -> log.warn("No user '{}' found; run Flyway migrations before starting.", username));
        } catch (Exception e) {
            log.error("Failed to seed the administrator password: {}", e.getMessage(), e);
        }
    }

    /** Marker hash written by V2__seed.sql; means "never set a real password". */
    private static boolean isPlaceholderHash(String hash) {
        return hash == null || hash.isBlank() || hash.contains("R2FpdGVzdZWNyZXQ");
    }

    private void openBrowser(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(java.net.URI.create(url));
                    return;
                } catch (Exception e) {
                    log.warn("Desktop.browse() failed: {}, trying xdg-open", e.getMessage());
                }
            }
            Runtime.getRuntime().exec(new String[]{"xdg-open", url});
        } catch (Exception e) {
            log.warn("Could not open browser automatically: {}", e.getMessage());
        }
        log.info("Open this URL manually: {}", url);
    }

    private void copyToClipboard(String text) {
        try {
            StringSelection selection = new StringSelection(text);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
        } catch (Exception e) {
            log.warn("Could not copy to clipboard: {}", e.getMessage());
        }
    }
}
