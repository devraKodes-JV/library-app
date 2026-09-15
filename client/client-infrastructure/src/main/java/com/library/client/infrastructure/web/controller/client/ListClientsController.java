package com.library.client.infrastructure.web.controller.client;

import java.util.List;
import java.util.Map;

import com.library.client.application.dto.response.client.ClientResponseDTO;
import com.library.client.application.service.client.ListClientsUseCase;
import com.library.config.application.service.ConfigService;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ListClientsController extends BaseController {

    private final ListClientsUseCase listClientsUseCase;
    private final ConfigService configService;

    public ListClientsController(ListClientsUseCase listClientsUseCase,
                                  ConfigService configService,
                                  WebControllerContext webContext) {
        super(webContext);
        this.listClientsUseCase = listClientsUseCase;
        this.configService = configService;
    }

    public void listClients(Context ctx) {
        requireCan(ctx, "clients.read");
        String status = ctx.queryParamAsClass("status", String.class).getOrDefault("active");
        configService.reload();
        List<ClientResponseDTO> clients = listClientsUseCase.execute(status);
        String membershipFeeYearly = configService.get("client", "membership.fee.yearly", "500.00");
        String membershipFeeMonthly = configService.get("client", "membership.fee.monthly", "50.00");
        String currency = configService.get("accounting", "currency", "USD");
        ctx.render("client/clients/list", buildListModel(ctx, Map.of(
                "clients", clients,
                "status", status,
                "membershipFeeYearly", membershipFeeYearly,
                "membershipFeeMonthly", membershipFeeMonthly,
                "currency", currency)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "clients.create"));
        model.put("canUpdate", hasPermission(ctx, "clients.update"));
        model.put("canDelete", hasPermission(ctx, "clients.delete"));
        model.put("canReactivate", hasPermission(ctx, "clients.reactivate"));
        model.put("canUpgrade", hasPermission(ctx, "clients.upgrade"));
        model.putAll(extra);
        return model;
    }
}
