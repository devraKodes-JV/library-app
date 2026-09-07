package com.library.client.infrastructure.web.controller.client;

import com.library.client.application.dto.command.client.DeleteClientCommand;
import com.library.client.application.service.client.DeleteClientUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class DeleteClientController extends BaseController {

    private final DeleteClientUseCase deleteClientUseCase;

    public DeleteClientController(DeleteClientUseCase deleteClientUseCase, WebControllerContext webContext) {
        super(webContext);
        this.deleteClientUseCase = deleteClientUseCase;
    }

    public void deleteClient(Context ctx) {
        requireCan(ctx, "clients.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteClientUseCase.execute(new DeleteClientCommand(id));
        flashWarning(ctx, "Client deleted.");
        ctx.redirect("/clients");
    }
}
