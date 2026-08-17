package com.library.bootstrap;

import java.awt.Desktop;
import java.net.InetAddress;
import java.net.URI;


import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.bootstrap.config.AppConfig;
import com.library.bootstrap.config.FlywayMigrations;
import com.library.bootstrap.config.JavalinStart;
import com.library.bootstrap.config.hibernate.HibernateConfiguration;
import com.library.bootstrap.upload.ImageStorage;

public class LibraryApplication {

    private static final Logger log = LoggerFactory.getLogger(LibraryApplication.class);

    private LibraryApplication() {
    }

    public static void main(String[] args) throws Exception {
        ImageStorage.init();
        FlywayMigrations.run(log);

        SessionFactory sessionFactory
                = HibernateConfiguration.buildHibernateConfiguration().buildSessionFactory();

        JavalinStart.run(sessionFactory, log); 

        String host = InetAddress.getLocalHost().getHostAddress();
        String url = "http://" + host + ":" + AppConfig.PORT + "/login";
        log.info("Application started at {}", url);
        openBrowser(url);
    }

    private static void openBrowser(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(url));
            } else {
                log.info("Desktop not supported; open manually: {}", url);
            }
        } catch (Exception e) {
            log.warn("Could not open browser automatically: {}", e.getMessage());
        }
    }
}
