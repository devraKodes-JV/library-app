package com.library.bootstrap.config.hibernate;

import org.hibernate.cfg.Configuration;

import com.library.bootstrap.config.AppConfig;

public class HibernateConfiguration {

    /**
     * Builds the Hibernate {@link org.hibernate.cfg.Configuration}.
     *
     * <p>
     * Entities are registered explicitly via {@code addAnnotatedClass} so there
     * is no classpath scanning (which is faster and more predictable in a fat
     * JAR).</p>
     *
     * @return a configured Hibernate {@link Configuration}
     */
    public static Configuration buildHibernateConfiguration() {
        Configuration cfg = new Configuration();

        cfg.setProperty("hibernate.connection.url", AppConfig.DB_URL);
        cfg.setProperty("hibernate.connection.username", "sa");
        cfg.setProperty("hibernate.connection.password", "");
        cfg.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        // Hibernate must NOT touch the schema: Flyway owns it.
        cfg.setProperty("hibernate.hbm2ddl.auto", "validate");
        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        cfg.setProperty("hibernate.show_sql", "false");
        cfg.setProperty("hibernate.format_sql", "true");

        // Register the JPA-mapped entities.
        IamAnnotatedClases.annotate(cfg);
        BooksAnnotatedClases.annotate(cfg);

        return cfg;
    }
}
