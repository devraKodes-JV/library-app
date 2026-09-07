package com.library.client.infrastructure.web.controller.client;

import java.util.List;
import java.util.Map;

import com.library.client.application.dto.response.client.ClientBenefitsDTO;
import com.library.client.application.dto.response.client.ClientDetailResponseDTO;
import com.library.client.application.service.client.GetClientBenefitsUseCase;
import com.library.client.application.service.client.GetClientUseCase;
import com.library.client.domain.exception.ClientNotFoundException;
import com.library.client.domain.model.ClientType;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ShowClientController extends BaseController {

    private final GetClientUseCase getClientUseCase;
    private final GetClientBenefitsUseCase getClientBenefitsUseCase;

    public ShowClientController(GetClientUseCase getClientUseCase, GetClientBenefitsUseCase getClientBenefitsUseCase, WebControllerContext webContext) {
        super(webContext);
        this.getClientUseCase = getClientUseCase;
        this.getClientBenefitsUseCase = getClientBenefitsUseCase;
    }

    public void showClient(Context ctx) {
        requireCan(ctx, "clients.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        ClientDetailResponseDTO client;
        try {
            client = getClientUseCase.execute(id);
        } catch (ClientNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("Client not found");
        }
        ClientType clientType = parseClientType(client.type());
        ClientBenefitsDTO benefits = getClientBenefitsUseCase.execute(clientType);
        ctx.render("client/clients/show", buildShowModel(ctx, Map.of(
                "client", client,
                "benefits", benefits)));
    }

    private ClientType parseClientType(String typeName) {
        if (typeName == null) {
            return ClientType.CASUAL;
        }
        try {
            return ClientType.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            return ClientType.CASUAL;
        }
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", hasPermission(ctx, "clients.update"));
        model.put("canDelete", hasPermission(ctx, "clients.delete"));
        model.putAll(extra);
        return model;
    }
}
