package com.library.books.infrastructure.web.controller.work;

import com.library.books.application.dto.command.work.DeleteWorkCommand;
import com.library.books.application.service.work.DeleteWorkUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class DeleteWorkController {

    private final DeleteWorkUseCase deleteWorkUseCase;
    private final WebControllerContext webContext;

    public DeleteWorkController(DeleteWorkUseCase deleteWorkUseCase, WebControllerContext webContext) {
        this.deleteWorkUseCase = deleteWorkUseCase;
        this.webContext = webContext;
    }

    public void deleteWork(Context ctx) {
        requireCan(ctx, "works.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteWorkUseCase.execute(new DeleteWorkCommand(id));
        WebHelper.flashWarning(ctx, "Work deleted.");
        ctx.redirect("/books/works");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
