package com.library.bootstrap.factory;

import org.hibernate.SessionFactory;

import com.library.kernel.web.WebControllerContext;
import com.library.security.SecurityFactory;
import com.library.bootstrap.factory.IamFactory;
import com.library.bootstrap.factory.BooksFactory;

import io.javalin.config.JavalinConfig;

/**
 * Orchestrator of the application's feature factories.
 *
 * <p>This is the single composition point for the whole monolith. Each feature
 * (IAM today, books tomorrow) exposes a {@code register(Javalin,
 * SessionFactory)} method and is wired here. The {@code LibraryApplication}
 * entry point only calls {@link #create(SessionFactory, Javalin)} and does not
 * know about any concrete dependency.</p>
 */
public final class AppFactory {

    private AppFactory() {
        // Utility class: no instantiation.
    }

    /**
     * Bootstraps every feature on the given Javalin config.
     *
     * @param sessionFactory the Hibernate session factory shared by all features
     * @param config         the Javalin configuration (routes are added here)
     */
    public static void create(SessionFactory sessionFactory, JavalinConfig config) {
        SecurityFactory.register(config, sessionFactory);

        WebControllerContext webContext = IamFactory.register(config, sessionFactory);
        BooksFactory.register(config, sessionFactory, webContext);
    }
}
