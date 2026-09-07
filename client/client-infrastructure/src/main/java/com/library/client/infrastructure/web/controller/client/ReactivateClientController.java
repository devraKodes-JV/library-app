package com.library.client.infrastructure.web.controller.client;

import com.library.client.application.dto.command.client.ReactivateClientCommand;
import com.library.client.application.service.client.ReactivateClientUseCase;
import com.library.client.domain.exception.ClientNotFoundException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ReactivateClientController extends BaseController {

    private final ReactivateClientUseCase reactivateClientUseCase;

    public ReactivateClientController(ReactivateClientUseCase reactivateClientUseCase, WebControllerContext webContext) {
        super(webContext);
        this.reactivateClientUseCase = reactivateClientUseCase;
    }

    public void reactivateClient(Context ctx) {
        requireCan(ctx, "clients.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivateClientUseCase.execute(new ReactivateClientCommand(id));
            flashSuccess(ctx, "Client reactivated.");
        } catch (ClientNotFoundException e) {
            flashDanger(ctx, "Client not found: " + id);
        }
        ctx.redirect("/clients");
    }
}
