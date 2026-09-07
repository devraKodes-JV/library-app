package com.library.reservation.infrastructure.web.controller.reservation;

import com.library.reservation.application.dto.command.reservation.RenewReservationCommand;
import com.library.reservation.application.service.reservation.RenewReservationUseCase;
import com.library.reservation.domain.exception.ReservationNotFoundException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;
import com.library.kernel.web.BaseController;

public class RenewReservationController extends BaseController {

    private final RenewReservationUseCase renewReservationUseCase;

    public RenewReservationController(RenewReservationUseCase renewReservationUseCase, WebControllerContext webContext) {
        super(webContext);
        this.renewReservationUseCase = renewReservationUseCase;
    }

    public void renewReservation(Context ctx) {
        requireCan(ctx, "reservations.renew");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            renewReservationUseCase.execute(new RenewReservationCommand(id));
            flashSuccess(ctx, "Reservation renewed.");
        } catch (ReservationNotFoundException e) {
            flashDanger(ctx, "Reservation not found: " + id);
        }
        ctx.redirect("/reservations");
    }
}
