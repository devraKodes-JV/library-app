package com.library.bootstrap;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.library.bootstrap.config.AppConfig;

public class LibraryApplication {

    // Runs before the `log` field below: slf4j-simple reads its configuration on
    // first use, so the log file has to be configured before any logger exists.
    static {
        configureFileLogging();
    }

    private static final Logger log = LoggerFactory.getLogger(LibraryApplication.class);

    private LibraryApplication() {
    }

    public static void main(String[] args) {
        ensureHostsEntry();
        SwingUtilities.invokeLater(() -> {
            new ServerWindow().setVisible(true);
        });
    }

    /**
     * Mirrors the console output into {@code ./data/librora.log}.
     *
     * <p>On Windows and macOS the packaged app has no console window, so without
     * this there would be no way to diagnose a failed start on the user's
     * machine.</p>
     */
    private static void configureFileLogging() {
        try {
            java.nio.file.Path dir = java.nio.file.Path.of("data");
            java.nio.file.Files.createDirectories(dir);
            String logFile = dir.resolve("librora.log").toAbsolutePath().toString();
            System.setProperty("org.slf4j.simpleLogger.logFile", logFile);
            System.setProperty("org.slf4j.simpleLogger.dateTimeFormat", "yyyy-MM-dd HH:mm:ss");
            System.setProperty("org.slf4j.simpleLogger.showThreadName", "false");
        } catch (Exception e) {
            // Non-fatal: console output still works.
            System.err.println("Could not configure file logging: " + e.getMessage());
        }
    }

    private static void ensureHostsEntry() {
        // When launched from the AppImage the AppRun script already performs the
        // full DNS/mDNS setup (avahi *and* the /etc/hosts fallback), so the Java
        // side must not repeat it.
        if (System.getenv("APPIMAGE") != null) {
            log.info("Running inside AppImage: DNS handled by AppRun.");
            return;
        }
        try {
            String localhost = InetAddress.getLocalHost().getHostAddress();
            Path hosts = Path.of("/etc/hosts");
            String content = Files.readString(hosts);
            if (content.contains("librora.local")) {
                log.info("Hosts entry already exists for librora.local");
                return;
            }
            // Fall back to /etc/hosts (via sudo) when avahi/sudo is unavailable.
            List<String> cmd = new ArrayList<>();
            cmd.add("sudo");
            cmd.add("-n");
            cmd.add("bash");
            cmd.add("-c");
            cmd.add("printf '%s librora.local\n' '" + localhost + "' >> /etc/hosts");
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            Process proc = pb.start();
            int rc = proc.waitFor();
            if (rc == 0) {
                log.info("Added hosts entry: {} librora.local", localhost);
            } else {
                log.warn("Cannot write /etc/hosts without sudo. Run manually: sudo sh -c \"echo '{} librora.local' >> /etc/hosts\". Access via localhost:{} instead.",
                        localhost, AppConfig.PORT);
            }
        } catch (IOException e) {
            log.warn("Could not determine localhost address: {}", e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while writing hosts entry.");
        }
    }
}
