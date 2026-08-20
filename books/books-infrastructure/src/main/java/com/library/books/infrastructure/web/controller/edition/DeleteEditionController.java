package com.library.books.infrastructure.web.controller.edition;

import com.library.books.application.dto.command.edition.DeleteEditionCommand;
import com.library.books.application.service.edition.DeleteEditionUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class DeleteEditionController extends BaseController {

    private final DeleteEditionUseCase deleteEditionUseCase;

    public DeleteEditionController(DeleteEditionUseCase deleteEditionUseCase, WebControllerContext webContext) {
        super(webContext);
        this.deleteEditionUseCase = deleteEditionUseCase;
    }

    public void deleteEdition(Context ctx) {
        requireCan(ctx, "editions.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteEditionUseCase.execute(new DeleteEditionCommand(id));
        flashWarning(ctx, "Edition deleted.");
        ctx.redirect("/books/editions");
    }

}