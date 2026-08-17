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
    // HTTP port the server listens on.
    public static final int PORT = 8080;

    // H2 database file location (fields are created next to the app).
    public static final String DB_URL = "jdbc:h2:file:./data/library;MODE=PostgreSQL;DB_CLOSE_ON_EXIT=FALSE";

    // public static String APP_URL = "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + PORT + "/login";
    
}
