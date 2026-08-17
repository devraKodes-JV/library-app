package com.library.bootstrap.config;

import org.slf4j.Logger;

public class FlywayMigrations {

    public static void run(Logger log) {
        log.info("Running Flyway migrations against H2 database.");
        var flyway = org.flywaydb.core.Flyway.configure()
                .dataSource(AppConfig.DB_URL, "sa", "")
                .cleanDisabled(false)
                .baselineOnMigrate(true)
                .load();
        flyway.clean();
        flyway.migrate();
        log.info("Flyway migrations applied.");
    }
}
