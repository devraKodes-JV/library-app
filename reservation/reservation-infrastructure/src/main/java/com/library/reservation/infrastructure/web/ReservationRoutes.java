package com.library.reservation.infrastructure.web;

import com.library.reservation.infrastructure.web.controller.reservation.CancelReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.CreateReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.FulfillReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.ListReservationsController;
import com.library.reservation.infrastructure.web.controller.reservation.RenewReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.ReturnReservationController;
import com.library.reservation.infrastructure.web.controller.reservation.ShowReservationController;

import io.javalin.config.JavalinConfig;

public final class ReservationRoutes {

    private ReservationRoutes() {
    }

    public static void register(JavalinConfig config,
                                ListReservationsController listReservationsController,
                                ShowReservationController showReservationController,
                                CreateReservationController createReservationController,
                                CancelReservationController cancelReservationController,
                                FulfillReservationController fulfillReservationController,
                                ReturnReservationController returnReservationController,
                                RenewReservationController renewReservationController) {

        config.routes.get("/reservations", listReservationsController::listReservations);
        config.routes.get("/reservations/new", createReservationController::showCreateForm);
        config.routes.post("/reservations", createReservationController::createReservation);
        config.routes.get("/reservations/{id}", showReservationController::showReservation);
        config.routes.post("/reservations/{id}/cancel", cancelReservationController::cancelReservation);
        config.routes.post("/reservations/{id}/fulfill", fulfillReservationController::fulfillReservation);
        config.routes.post("/reservations/{id}/return", returnReservationController::returnReservation);
        config.routes.get("/reservations/{id}/return/preview", returnReservationController::returnReservationPreview);
        config.routes.post("/reservations/{id}/renew", renewReservationController::renewReservation);
    }
}
