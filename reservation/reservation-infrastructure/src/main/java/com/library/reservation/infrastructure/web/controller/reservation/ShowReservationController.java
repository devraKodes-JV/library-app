package com.library.reservation.infrastructure.web.controller.reservation;

import java.util.List;
import java.util.Map;

import com.library.reservation.application.dto.response.reservation.ReservationResponseDTO;
import com.library.reservation.application.service.reservation.GetReservationUseCase;
import com.library.reservation.domain.exception.ReservationNotFoundException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;
import com.library.kernel.web.BaseController;

public class ShowReservationController extends BaseController {

    private final GetReservationUseCase getReservationUseCase;

    public ShowReservationController(GetReservationUseCase getReservationUseCase, WebControllerContext webContext) {
        super(webContext);
        this.getReservationUseCase = getReservationUseCase;
    }

    public void showReservation(Context ctx) {
        requireCan(ctx, "reservations.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        ReservationResponseDTO reservation;
        try {
            reservation = getReservationUseCase.execute(id);
        } catch (ReservationNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("Reservation not found");
        }
        boolean incomplete = reservation.clientDataIncomplete();
        boolean canFulfill = hasPermission(ctx, "reservations.fulfill");
        boolean showFulfillWizard = canFulfill && "DEPOSIT_PENDING".equals(reservation.status());
        ctx.render("reservation/reservations/show", buildShowModel(ctx, Map.of(
                "reservation", reservation,
                "showClientModal", incomplete,
                "showFulfillWizard", showFulfillWizard,
                "currentUserName", currentUserName(ctx))));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", hasPermission(ctx, "reservations.update"));
        model.put("canDelete", hasPermission(ctx, "reservations.delete"));
        model.put("canCancel", hasPermission(ctx, "reservations.cancel"));
        model.put("canFulfill", hasPermission(ctx, "reservations.fulfill"));
        model.put("canReturn", hasPermission(ctx, "reservations.return"));
        model.put("canRenew", hasPermission(ctx, "reservations.renew"));
        model.putAll(extra);
        return model;
    }
}
