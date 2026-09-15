package com.library.catalog.infrastructure.web;

import com.library.catalog.infrastructure.web.controller.CatalogController;

import io.javalin.config.JavalinConfig;

public final class CatalogRoutes {

    private CatalogRoutes() {
    }

    public static void register(JavalinConfig config, CatalogController catalogController) {
        config.routes.get("/catalog", catalogController::showCatalog);
    }
}
