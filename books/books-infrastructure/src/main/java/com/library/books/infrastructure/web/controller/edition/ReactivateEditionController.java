package com.library.books.infrastructure.web.controller.edition;

import com.library.books.application.dto.command.edition.ReactivateEditionCommand;
import com.library.books.application.service.edition.ReactivateEditionUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ReactivateEditionController extends BaseController {

    private final ReactivateEditionUseCase reactivateEditionUseCase;

    public ReactivateEditionController(ReactivateEditionUseCase reactivateEditionUseCase, WebControllerContext webContext) {
        super(webContext);
        this.reactivateEditionUseCase = reactivateEditionUseCase;
    }

    public void reactivateEdition(Context ctx) {
        requireCan(ctx, "editions.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivateEditionUseCase.execute(new ReactivateEditionCommand(id));
            flashSuccess(ctx, "Edition reactivated.");
        } catch (com.library.books.domain.exception.ReactivationException e) {
            flashDanger(ctx, e.getMessage());
        } catch (com.library.books.domain.exception.EditionNotFoundException e) {
            flashDanger(ctx, "Edition not found: " + id);
        }
        ctx.redirect("/books/editions");
    }
}
