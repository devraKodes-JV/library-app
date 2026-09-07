package com.library.reservation.infrastructure.web.controller.reservation;

import com.library.reservation.application.dto.command.reservation.ReturnReservationCommand;
import com.library.reservation.application.service.reservation.ReturnReservationUseCase;
import com.library.reservation.domain.exception.ReservationNotFoundException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import com.library.kernel.web.BaseController;

public class ReturnReservationController extends BaseController {

    private final ReturnReservationUseCase returnReservationUseCase;

    public ReturnReservationController(ReturnReservationUseCase returnReservationUseCase, WebControllerContext webContext) {
        super(webContext);
        this.returnReservationUseCase = returnReservationUseCase;
    }

    public void returnReservationPreview(Context ctx) {
        requireCan(ctx, "reservations.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            var b = returnReservationUseCase.previewById(id);
            ctx.json(java.util.Map.of(
                    "lateFee", b.lateFee(),
                    "deposit", b.deposit(),
                    "settlement", b.settlement()
            ));
        } catch (ReservationNotFoundException e) {
            throw new NotFoundResponse("Reservation not found: " + id);
        }
    }

    public void returnReservation(Context ctx) {
        requireCan(ctx, "reservations.return");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        String paymentMethod = ctx.formParam("paymentMethod");
        String receivedBy = currentUserName(ctx);
        boolean paymentConfirmed = "true".equalsIgnoreCase(ctx.formParam("paymentConfirmed"))
                || "on".equalsIgnoreCase(ctx.formParam("paymentConfirmed"));
        boolean ajax = isAjax(ctx);
        try {
            returnReservationUseCase.execute(new ReturnReservationCommand(id, paymentMethod, receivedBy, paymentConfirmed));
            if (ajax) {
                ctx.status(200).json(java.util.Map.of("status", "ok", "reservationId", id));
            } else {
                flashSuccess(ctx, "Reservation returned.");
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
