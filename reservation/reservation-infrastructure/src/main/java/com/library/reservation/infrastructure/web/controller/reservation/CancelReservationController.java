package com.library.reservation.infrastructure.web.controller.reservation;

import com.library.reservation.application.dto.command.reservation.CancelReservationCommand;
import com.library.reservation.application.service.reservation.CancelReservationUseCase;
import com.library.reservation.domain.exception.ReservationNotFoundException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;
import com.library.kernel.web.BaseController;

public class CancelReservationController extends BaseController {

    private final CancelReservationUseCase cancelReservationUseCase;

    public CancelReservationController(CancelReservationUseCase cancelReservationUseCase, WebControllerContext webContext) {
        super(webContext);
        this.cancelReservationUseCase = cancelReservationUseCase;
    }

    public void cancelReservation(Context ctx) {
        requireCan(ctx, "reservations.cancel");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            cancelReservationUseCase.execute(new CancelReservationCommand(id));
            flashSuccess(ctx, "Reservation cancelled.");
        } catch (ReservationNotFoundException e) {
            flashDanger(ctx, "Reservation not found: " + id);
        }
        ctx.redirect("/reservations");
    }
}
