package com.library.books.infrastructure.web.controller.bookFormat;

import com.library.books.application.dto.command.bookFormat.DeleteBookFormatCommand;
import com.library.books.application.service.bookFormat.DeleteBookFormatUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class DeleteBookFormatController extends BaseController {

    private final DeleteBookFormatUseCase deleteBookFormatUseCase;

    public DeleteBookFormatController(DeleteBookFormatUseCase deleteBookFormatUseCase, WebControllerContext webContext) {
        super(webContext);
        this.deleteBookFormatUseCase = deleteBookFormatUseCase;
    }

    public void deleteFormat(Context ctx) {
        requireCan(ctx, "formats.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteBookFormatUseCase.execute(new DeleteBookFormatCommand(id));
        flashWarning(ctx, "Format deleted.");
        ctx.redirect("/books/formats");
    }

}