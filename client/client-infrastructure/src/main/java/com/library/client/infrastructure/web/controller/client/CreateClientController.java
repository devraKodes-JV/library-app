package com.library.client.infrastructure.web.controller.client;

import java.util.List;
import java.util.Map;

import com.library.client.application.dto.command.client.CreateClientCommand;
import com.library.client.application.dto.response.client.ClientResponseDTO;
import com.library.client.application.service.client.CreateClientUseCase;
import com.library.client.application.service.client.ListClientsUseCase;
import com.library.client.domain.exception.ValidationException;
import com.library.client.domain.model.ClientType;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class CreateClientController extends BaseController {

    private final CreateClientUseCase createClientUseCase;
    private final ListClientsUseCase listClientsUseCase;

    public CreateClientController(CreateClientUseCase createClientUseCase, ListClientsUseCase listClientsUseCase, WebControllerContext webContext) {
        super(webContext);
        this.createClientUseCase = createClientUseCase;
        this.listClientsUseCase = listClientsUseCase;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "clients.create");
        List<ClientResponseDTO> clients = listClientsUseCase.execute();
        ctx.render("client/clients/form", buildCreateModel(ctx, Map.of("clients", clients)));
    }

    public void createClient(Context ctx) {
        requireCan(ctx, "clients.create");
        CreateClientCommand command = new CreateClientCommand(
                ctx.formParam("dni"),
                ctx.formParam("fullName"),
                ctx.formParam("email"),
                ctx.formParam("phone"),
                ctx.formParam("address"),
                parseClientType(ctx.formParam("type")),
                parseLocalDate(ctx.formParam("memberUntil")),
                parseLocalDate(ctx.formParam("birthDate")),
                ctx.formParam("notes"));

        try {
            createClientUseCase.execute(command);
            flashSuccess(ctx, "Client created successfully.");
            ctx.redirect("/clients");
        } catch (ValidationException e) {
            List<ClientResponseDTO> clients = listClientsUseCase.execute();
            Map<String, Object> model = buildCreateModel(ctx, Map.of("clients", clients));
            model.putAll(e.getFieldErrors());
            ctx.render("client/clients/form", model);
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("client", null);
        model.put("clientTypes", ClientType.values());
        model.put("user", current);
        model.put("navSections", navSections);
        model.putAll(extra);
        return model;
    }

    private ClientType parseClientType(String value) {
        if (value == null || value.isBlank()) {
            return ClientType.CASUAL;
        }
        try {
            return ClientType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ClientType.CASUAL;
        }
    }

    private java.time.LocalDate parseLocalDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return java.time.LocalDate.parse(value);
        } catch (java.time.format.DateTimeParseException e) {
            return null;
        }
    }
}
