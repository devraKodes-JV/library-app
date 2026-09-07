package com.library.reservation.infrastructure.web.controller.reservation;

import com.library.reservation.application.dto.command.reservation.FulfillReservationCommand;
import com.library.reservation.application.service.reservation.FulfillReservationUseCase;
import com.library.reservation.domain.exception.ReservationNotFoundException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;
import com.library.kernel.web.BaseController;

public class FulfillReservationController extends BaseController {

    private final FulfillReservationUseCase fulfillReservationUseCase;

    public FulfillReservationController(FulfillReservationUseCase fulfillReservationUseCase, WebControllerContext webContext) {
        super(webContext);
        this.fulfillReservationUseCase = fulfillReservationUseCase;
    }

    public void fulfillReservation(Context ctx) {
        requireCan(ctx, "reservations.fulfill");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        String paymentMethod = ctx.formParam("paymentMethod");
        String receivedBy = currentUserName(ctx);
        boolean paymentConfirmed = "true".equalsIgnoreCase(ctx.formParam("paymentConfirmed"))
                || "on".equalsIgnoreCase(ctx.formParam("paymentConfirmed"));
        boolean ajax = isAjax(ctx);
        try {
            fulfillReservationUseCase.execute(new FulfillReservationCommand(id, paymentMethod, receivedBy, paymentConfirmed));
            if (ajax) {
                ctx.status(200).json(java.util.Map.of("status", "ok", "reservationId", id));
            } else {
                flashSuccess(ctx, "Reservation fulfilled.");
                ctx.redirect("/reservations");
            }
        } catch (ReservationNotFoundException e) {
            if (ajax) ctx.status(404).json(java.util.Map.of("error", "Reservation not found: " + id));
            else { flashDanger(ctx, "Reservation not found: " + id); ctx.redirect("/reservations"); }
        } catch (IllegalStateException e) {
            if (ajax) ctx.status(409).json(java.util.Map.of("error", e.getMessage()));
            else { flashDanger(ctx, e.getMessage()); ctx.redirect("/reservations"); }
        }
    }

    private boolean isAjax(Context ctx) {
        String xrw = ctx.header("X-Requested-With");
        if (xrw != null && xrw.equalsIgnoreCase("XMLHttpRequest")) return true;
        String accept = ctx.header("Accept");
        return accept != null && accept.contains("application/json");
    }
}
