package com.library.books.infrastructure.web.controller.bookFormat;

import com.library.books.application.dto.command.bookFormat.ReactivateBookFormatCommand;
import com.library.books.application.service.bookFormat.ReactivateBookFormatUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ReactivateBookFormatController extends BaseController {

    private final ReactivateBookFormatUseCase reactivateBookFormatUseCase;

    public ReactivateBookFormatController(ReactivateBookFormatUseCase reactivateBookFormatUseCase, WebControllerContext webContext) {
        super(webContext);
        this.reactivateBookFormatUseCase = reactivateBookFormatUseCase;
    }

    public void reactivateBookFormat(Context ctx) {
        requireCan(ctx, "formats.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivateBookFormatUseCase.execute(new ReactivateBookFormatCommand(id));
            flashSuccess(ctx, "Book format reactivated.");
        } catch (com.library.books.domain.exception.ReactivationException e) {
            flashDanger(ctx, e.getMessage());
        } catch (com.library.books.domain.exception.BookFormatNotFoundException e) {
            flashDanger(ctx, "Book format not found: " + id);
        }
        ctx.redirect("/books/formats");
    }
}
