package com.library.client.infrastructure.web.controller.client;

import java.util.List;
import java.util.Map;

import com.library.client.application.dto.response.client.ClientDetailResponseDTO;
import com.library.client.application.service.client.GetClientUseCase;
import com.library.client.application.service.client.ListClientsUseCase;
import com.library.client.application.service.client.UpdateClientUseCase;
import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class UpdateClientController extends BaseController {

    private final UpdateClientUseCase updateClientUseCase;
    private final GetClientUseCase getClientUseCase;
    private final ListClientsUseCase listClientsUseCase;

    public UpdateClientController(UpdateClientUseCase updateClientUseCase,
                                  GetClientUseCase getClientUseCase,
                                  ListClientsUseCase listClientsUseCase,
                                  WebControllerContext webContext) {
        super(webContext);
        this.updateClientUseCase = updateClientUseCase;
        this.getClientUseCase = getClientUseCase;
        this.listClientsUseCase = listClientsUseCase;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "clients.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var client = getClientUseCase.execute(id);
        ctx.render("client/clients/edit", buildEditModel(ctx, Map.of("client", client)));
    }

    public void updateClient(Context ctx) {
        requireCan(ctx, "clients.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var command = new com.library.client.application.dto.command.client.UpdateClientCommand(
                id,
                ctx.formParam("fullName"),
                ctx.formParam("email"),
                ctx.formParam("phone"),
                ctx.formParam("address"),
                ctx.formParam("type") != null ? com.library.client.domain.model.ClientType.valueOf(ctx.formParam("type")) : null,
                ctx.formParam("memberUntil") != null && !ctx.formParam("memberUntil").isBlank() ? java.time.LocalDate.parse(ctx.formParam("memberUntil")) : null,
                ctx.formParam("birthDate") != null && !ctx.formParam("birthDate").isBlank() ? java.time.LocalDate.parse(ctx.formParam("birthDate")) : null,
                ctx.formParam("notes")
        );

        try {
            updateClientUseCase.execute(command);
            flashSuccess(ctx, "Client updated successfully.");
            ctx.redirect("/clients");
        } catch (Exception e) {
            flashDanger(ctx, "Error updating client: " + e.getMessage());
            ctx.redirect("/clients/" + id + "/edit");
        }
    }

    private Map<String, Object> buildEditModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.putAll(extra);
        return model;
    }
}
