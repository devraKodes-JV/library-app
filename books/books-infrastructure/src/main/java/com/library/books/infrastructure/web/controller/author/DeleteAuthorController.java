package com.library.books.infrastructure.web.controller.author;

import com.library.books.application.dto.command.author.DeleteAuthorCommand;
import com.library.books.application.service.author.DeleteAuthorUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class DeleteAuthorController {

    private final DeleteAuthorUseCase deleteAuthorUseCase;
    private final WebControllerContext webContext;

    public DeleteAuthorController(DeleteAuthorUseCase deleteAuthorUseCase, WebControllerContext webContext) {
        this.deleteAuthorUseCase = deleteAuthorUseCase;
        this.webContext = webContext;
    }

    public void deleteAuthor(Context ctx) {
        requireCan(ctx, "authors.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteAuthorUseCase.execute(new DeleteAuthorCommand(id));
        WebHelper.flashWarning(ctx, "Author deleted.");
        ctx.redirect("/books/authors");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
