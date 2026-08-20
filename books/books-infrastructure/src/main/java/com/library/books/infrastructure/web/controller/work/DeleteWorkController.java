package com.library.books.infrastructure.web.controller.work;

import com.library.books.application.dto.command.work.DeleteWorkCommand;
import com.library.books.application.service.work.DeleteWorkUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class DeleteWorkController extends BaseController {

    private final DeleteWorkUseCase deleteWorkUseCase;

    public DeleteWorkController(DeleteWorkUseCase deleteWorkUseCase, WebControllerContext webContext) {
        super(webContext);
        this.deleteWorkUseCase = deleteWorkUseCase;
    }

    public void deleteWork(Context ctx) {
        requireCan(ctx, "works.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteWorkUseCase.execute(new DeleteWorkCommand(id));
        flashWarning(ctx, "Work deleted.");
        ctx.redirect("/books/works");
    }

}