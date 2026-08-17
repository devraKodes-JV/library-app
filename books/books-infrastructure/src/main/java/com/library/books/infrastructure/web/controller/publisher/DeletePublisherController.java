package com.library.books.infrastructure.web.controller.publisher;

import com.library.books.application.dto.command.publisher.DeletePublisherCommand;
import com.library.books.application.service.publisher.DeletePublisherUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class DeletePublisherController {

    private final DeletePublisherUseCase deletePublisherUseCase;
    private final WebControllerContext webContext;

    public DeletePublisherController(DeletePublisherUseCase deletePublisherUseCase, WebControllerContext webContext) {
        this.deletePublisherUseCase = deletePublisherUseCase;
        this.webContext = webContext;
    }

    public void deletePublisher(Context ctx) {
        requireCan(ctx, "publishers.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deletePublisherUseCase.execute(new DeletePublisherCommand(id));
        WebHelper.flashWarning(ctx, "Publisher deleted.");
        ctx.redirect("/books/publishers");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
