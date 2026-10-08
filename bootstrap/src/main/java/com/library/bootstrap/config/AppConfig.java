package com.library.bootstrap.config;

import com.library.bootstrap.config.hibernate.HibernateConfiguration;

/**
 * Centralised application configuration constant(s).
 *
 * <p>Shared by {@link FlywayMigrations} and {@link HibernateConfiguration}
 * (and any future factory that needs a database connection) so the connection
 * URL is defined in exactly one place. If the database path ever changes, only
 * this class must be updated.</p>
 */
public final class AppConfig {
    // HTTP port the server listens on. Overridable with LIBRORA_PORT so AppRun
    // (which publishes mDNS on it) and the JVM can never disagree.
    public static final int PORT = intEnv("LIBRORA_PORT", 8080);

    // HTTPS port used by the offline/AppImage distribution (see TlsSupport).
    public static final int HTTPS_PORT = 8443;

    // H2 database file location (fields are created next to the app).
    // FILE_LOCK=NO + LOCK_TIMEOUT avoid the "Database may be already in use" error
    // when the JVM is killed without graceful shutdown (Ctrl+C, IDE stop, etc.).
    public static final String DB_URL = "jdbc:h2:file:./data/library;MODE=PostgreSQL;DB_CLOSE_ON_EXIT=FALSE;FILE_LOCK=NO;LOCK_TIMEOUT=10000";

    private AppConfig() {
    }

    private static int intEnv(String name, int fallback) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            return fallback;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            return (parsed > 0 && parsed <= 65535) ? parsed : fallback;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
