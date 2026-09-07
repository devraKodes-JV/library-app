package com.library.reservation.infrastructure.web.controller.reservation;

import java.util.List;
import java.util.Map;

import com.library.reservation.application.dto.response.reservation.ReservationResponseDTO;
import com.library.reservation.application.service.reservation.ListReservationsUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;
import com.library.kernel.web.BaseController;

public class ListReservationsController extends BaseController {

    private final ListReservationsUseCase listReservationsUseCase;

    public ListReservationsController(ListReservationsUseCase listReservationsUseCase, WebControllerContext webContext) {
        super(webContext);
        this.listReservationsUseCase = listReservationsUseCase;
    }

    public void listReservations(Context ctx) {
        requireCan(ctx, "reservations.read");
        String status = ctx.queryParamAsClass("status", String.class).getOrDefault("active");
        List<ReservationResponseDTO> reservations = listReservationsUseCase.execute(status);
        ctx.render("reservation/reservations/list", buildListModel(ctx, Map.of(
                "reservations", reservations,
                "status", status)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "reservations.create"));
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
