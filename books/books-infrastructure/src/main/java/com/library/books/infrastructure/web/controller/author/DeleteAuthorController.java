package com.library.books.infrastructure.web.controller.author;

import com.library.books.application.dto.command.author.DeleteAuthorCommand;
import com.library.books.application.service.author.DeleteAuthorUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class DeleteAuthorController extends BaseController {

    private final DeleteAuthorUseCase deleteAuthorUseCase;

    public DeleteAuthorController(DeleteAuthorUseCase deleteAuthorUseCase, WebControllerContext webContext) {
        super(webContext);
        this.deleteAuthorUseCase = deleteAuthorUseCase;
    }

    public void deleteAuthor(Context ctx) {
        requireCan(ctx, "authors.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteAuthorUseCase.execute(new DeleteAuthorCommand(id));
        flashWarning(ctx, "Author deleted.");
        ctx.redirect("/books/authors");
    }

}