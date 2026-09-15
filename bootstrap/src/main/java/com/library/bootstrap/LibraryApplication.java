package com.library.bootstrap;

import java.awt.Desktop;
import java.net.InetAddress;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.bootstrap.config.AppConfig;
import com.library.bootstrap.config.FlywayMigrations;
import com.library.bootstrap.config.JavalinStart;
import com.library.bootstrap.config.hibernate.HibernateConfiguration;
import com.library.bootstrap.factory.AppFactory;
import com.library.bootstrap.factory.IamFactory;
import com.library.bootstrap.scheduler.MembershipExpiryScheduler;
import com.library.bootstrap.upload.ImageStorage;
import com.library.iam.domain.port.out.UserPort;
import com.library.iam.infrastructure.security.BouncyCastleArgon2PasswordHasher;

public class LibraryApplication {

    private static final Logger log = LoggerFactory.getLogger(LibraryApplication.class);

    private LibraryApplication() {
    }

    public static void main(String[] args) throws Exception {
        ImageStorage.init();
        FlywayMigrations.run(log);

        SessionFactory sessionFactory
                = HibernateConfiguration.buildHibernateConfiguration().buildSessionFactory();

        seedDefaultPasswords(sessionFactory);

        JavalinStart.run(sessionFactory, log);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "membership-expiry-scheduler");
            t.setDaemon(true);
            return t;
        });
        scheduler.scheduleAtFixedRate(
                () -> MembershipExpiryScheduler.run(sessionFactory),
                1, 60, TimeUnit.MINUTES);

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

    private static void seedDefaultPasswords(SessionFactory sessionFactory) {
        try {
            UserPort userPort = IamFactory.userPort(sessionFactory);
            BouncyCastleArgon2PasswordHasher hasher = new BouncyCastleArgon2PasswordHasher();

            java.util.Map<String, String> defaults = java.util.Map.of(
                    "admin", "admin123",
                    "employee", "employee123"
            );

            for (java.util.Map.Entry<String, String> entry : defaults.entrySet()) {
                userPort.findByUsername(entry.getKey()).ifPresent(user -> {
                    String newHash = hasher.hash(entry.getValue());
                    userPort.updatePassword(entry.getKey(), newHash);
                    log.info("Default password reset for user={}", entry.getKey());
                });
            }
        } catch (Exception e) {
            log.error("Failed to seed default passwords: {}", e.getMessage(), e);
        }
    }
}
