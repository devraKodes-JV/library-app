package com.library.reservation.infrastructure.web;

import com.library.reservation.infrastructure.web.controller.catalog.CatalogController;
import com.library.reservation.infrastructure.web.controller.catalog.GuestReservationController;

import io.javalin.config.JavalinConfig;

public final class CatalogRoutes {

    private CatalogRoutes() {
    }

    public static void register(JavalinConfig config,
                                CatalogController catalogController,
                                GuestReservationController guestReservationController) {

        config.routes.get("/catalog", catalogController::showCatalog);
        config.routes.get("/catalog/reserve/{editionId}", guestReservationController::showReserveForm);
        config.routes.post("/catalog/reserve/{editionId}", guestReservationController::createReservation);
        config.routes.get("/catalog/reservation/success", ctx -> ctx.render("reservation/catalog/success.html"));
    }
}
