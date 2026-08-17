package com.library.books.infrastructure.web.controller.bookFormat;

import com.library.books.application.dto.command.bookFormat.DeleteBookFormatCommand;
import com.library.books.application.service.bookFormat.DeleteBookFormatUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class DeleteBookFormatController {

    private final DeleteBookFormatUseCase deleteBookFormatUseCase;
    private final WebControllerContext webContext;

    public DeleteBookFormatController(DeleteBookFormatUseCase deleteBookFormatUseCase, WebControllerContext webContext) {
        this.deleteBookFormatUseCase = deleteBookFormatUseCase;
        this.webContext = webContext;
    }

    public void deleteFormat(Context ctx) {
        requireCan(ctx, "formats.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteBookFormatUseCase.execute(new DeleteBookFormatCommand(id));
        WebHelper.flashWarning(ctx, "Format deleted.");
        ctx.redirect("/books/formats");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
