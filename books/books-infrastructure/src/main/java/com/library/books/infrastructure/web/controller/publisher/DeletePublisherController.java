package com.library.books.infrastructure.web.controller.publisher;

import com.library.books.application.dto.command.publisher.DeletePublisherCommand;
import com.library.books.application.service.publisher.DeletePublisherUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class DeletePublisherController extends BaseController {

    private final DeletePublisherUseCase deletePublisherUseCase;

    public DeletePublisherController(DeletePublisherUseCase deletePublisherUseCase, WebControllerContext webContext) {
        super(webContext);
        this.deletePublisherUseCase = deletePublisherUseCase;
    }

    public void deletePublisher(Context ctx) {
        requireCan(ctx, "publishers.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deletePublisherUseCase.execute(new DeletePublisherCommand(id));
        flashWarning(ctx, "Publisher deleted.");
        ctx.redirect("/books/publishers");
    }

}