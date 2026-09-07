package com.library.reservation.infrastructure.web.controller.reservation;

import java.util.List;
import java.util.Map;

import com.library.client.application.dto.response.client.ClientResponseDTO;
import com.library.client.application.service.client.ListClientsUseCase;
import com.library.reservation.application.dto.command.reservation.CreateReservationCommand;
import com.library.reservation.application.dto.response.reservation.ReservationResponseDTO;
import com.library.reservation.application.service.reservation.CreateReservationUseCase;
import com.library.reservation.application.service.reservation.ListReservationsUseCase;
import com.library.reservation.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.StockItemDTO;
import com.library.stock.application.service.stockitem.ListStockItemsUseCase;

import io.javalin.http.Context;
import com.library.kernel.web.BaseController;

public class CreateReservationController extends BaseController {

    private final CreateReservationUseCase createReservationUseCase;
    private final ListReservationsUseCase listReservationsUseCase;
    private final ListClientsUseCase listClientsUseCase;
    private final ListStockItemsUseCase listStockItemsUseCase;

    public CreateReservationController(CreateReservationUseCase createReservationUseCase,
                                       ListReservationsUseCase listReservationsUseCase,
                                       ListClientsUseCase listClientsUseCase,
                                       ListStockItemsUseCase listStockItemsUseCase,
                                       WebControllerContext webContext) {
        super(webContext);
        this.createReservationUseCase = createReservationUseCase;
        this.listReservationsUseCase = listReservationsUseCase;
        this.listClientsUseCase = listClientsUseCase;
        this.listStockItemsUseCase = listStockItemsUseCase;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "reservations.create");
        List<ReservationResponseDTO> reservations = listReservationsUseCase.execute();
        List<ClientResponseDTO> clients = listClientsUseCase.execute("ACTIVE");
        List<StockItemDTO> stockItems = listStockItemsUseCase.execute("AVAILABLE");
        ctx.render("reservation/reservations/form", buildCreateModel(ctx, Map.of(
                "reservations", reservations,
                "clients", clients,
                "stockItems", stockItems)));
    }

    public void createReservation(Context ctx) {
        requireCan(ctx, "reservations.create");
        CreateReservationCommand command = new CreateReservationCommand(
                parseLong(ctx.formParam("clientId")),
                null,
                parseInteger(ctx.formParam("loanDays")),
                ctx.formParam("notes"));

        Long stockItemId = parseLong(ctx.formParam("stockItemId"));

        try {
            createReservationUseCase.execute(command, stockItemId);
            flashSuccess(ctx, "Reservation created successfully.");
            ctx.redirect("/reservations");
        } catch (ValidationException e) {
            List<ReservationResponseDTO> reservations = listReservationsUseCase.execute();
            List<ClientResponseDTO> clients = listClientsUseCase.execute("ACTIVE");
            List<StockItemDTO> stockItems = listStockItemsUseCase.execute("AVAILABLE");
            Map<String, Object> model = buildCreateModel(ctx, Map.of(
                    "reservations", reservations,
                    "clients", clients,
                    "stockItems", stockItems));
            model.putAll(e.getFieldErrors());
            ctx.render("reservation/reservations/form", model);
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("reservation", null);
        model.put("user", current);
        model.put("navSections", navSections);
        model.putAll(extra);
        return model;
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
