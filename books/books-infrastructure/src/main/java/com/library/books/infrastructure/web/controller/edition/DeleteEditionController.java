package com.library.books.infrastructure.web.controller.edition;

import com.library.books.application.dto.command.edition.DeleteEditionCommand;
import com.library.books.application.service.edition.DeleteEditionUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class DeleteEditionController {

    private final DeleteEditionUseCase deleteEditionUseCase;
    private final WebControllerContext webContext;

    public DeleteEditionController(DeleteEditionUseCase deleteEditionUseCase, WebControllerContext webContext) {
        this.deleteEditionUseCase = deleteEditionUseCase;
        this.webContext = webContext;
    }

    public void deleteEdition(Context ctx) {
        requireCan(ctx, "editions.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteEditionUseCase.execute(new DeleteEditionCommand(id));
        WebHelper.flashWarning(ctx, "Edition deleted.");
        ctx.redirect("/books/editions");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
