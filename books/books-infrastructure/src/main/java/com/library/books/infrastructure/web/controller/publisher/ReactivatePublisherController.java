package com.library.books.infrastructure.web.controller.publisher;

import com.library.books.application.dto.command.publisher.ReactivatePublisherCommand;
import com.library.books.application.service.publisher.ReactivatePublisherUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ReactivatePublisherController extends BaseController {

    private final ReactivatePublisherUseCase reactivatePublisherUseCase;

    public ReactivatePublisherController(ReactivatePublisherUseCase reactivatePublisherUseCase, WebControllerContext webContext) {
        super(webContext);
        this.reactivatePublisherUseCase = reactivatePublisherUseCase;
    }

    public void reactivatePublisher(Context ctx) {
        requireCan(ctx, "publishers.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivatePublisherUseCase.execute(new ReactivatePublisherCommand(id));
            flashSuccess(ctx, "Publisher reactivated.");
        } catch (com.library.books.domain.exception.ReactivationException e) {
            flashDanger(ctx, e.getMessage());
        } catch (com.library.books.domain.exception.PublisherNotFoundException e) {
            flashDanger(ctx, "Publisher not found: " + id);
        }
        ctx.redirect("/books/publishers");
    }
}
