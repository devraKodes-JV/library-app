package com.library.books.infrastructure.web.controller.author;

import com.library.books.application.dto.command.author.ReactivateAuthorCommand;
import com.library.books.application.service.author.ReactivateAuthorUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ReactivateAuthorController extends BaseController {

    private final ReactivateAuthorUseCase reactivateAuthorUseCase;

    public ReactivateAuthorController(ReactivateAuthorUseCase reactivateAuthorUseCase, WebControllerContext webContext) {
        super(webContext);
        this.reactivateAuthorUseCase = reactivateAuthorUseCase;
    }

    public void reactivateAuthor(Context ctx) {
        requireCan(ctx, "authors.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivateAuthorUseCase.execute(new ReactivateAuthorCommand(id));
            flashSuccess(ctx, "Author reactivated.");
        } catch (com.library.books.domain.exception.ReactivationException e) {
            flashDanger(ctx, e.getMessage());
        } catch (com.library.books.domain.exception.AuthorNotFoundException e) {
            flashDanger(ctx, "Author not found: " + id);
        }
        ctx.redirect("/books/authors");
    }
}
