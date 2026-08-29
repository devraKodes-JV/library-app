package com.library.books.infrastructure.web.controller.work;

import com.library.books.application.dto.command.work.ReactivateWorkCommand;
import com.library.books.application.service.work.ReactivateWorkUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ReactivateWorkController extends BaseController {

    private final ReactivateWorkUseCase reactivateWorkUseCase;

    public ReactivateWorkController(ReactivateWorkUseCase reactivateWorkUseCase, WebControllerContext webContext) {
        super(webContext);
        this.reactivateWorkUseCase = reactivateWorkUseCase;
    }

    public void reactivateWork(Context ctx) {
        requireCan(ctx, "works.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivateWorkUseCase.execute(new ReactivateWorkCommand(id));
            flashSuccess(ctx, "Work reactivated.");
        } catch (com.library.books.domain.exception.ReactivationException e) {
            flashDanger(ctx, e.getMessage());
        } catch (com.library.books.domain.exception.WorkNotFoundException e) {
            flashDanger(ctx, "Work not found: " + id);
        }
        ctx.redirect("/books/works");
    }
}
