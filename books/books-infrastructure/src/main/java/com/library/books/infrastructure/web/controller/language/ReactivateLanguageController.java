package com.library.books.infrastructure.web.controller.language;

import com.library.books.application.dto.command.language.ReactivateLanguageCommand;
import com.library.books.application.service.language.ReactivateLanguageUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ReactivateLanguageController extends BaseController {

    private final ReactivateLanguageUseCase reactivateLanguageUseCase;

    public ReactivateLanguageController(ReactivateLanguageUseCase reactivateLanguageUseCase, WebControllerContext webContext) {
        super(webContext);
        this.reactivateLanguageUseCase = reactivateLanguageUseCase;
    }

    public void reactivateLanguage(Context ctx) {
        requireCan(ctx, "languages.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivateLanguageUseCase.execute(new ReactivateLanguageCommand(id));
            flashSuccess(ctx, "Language reactivated.");
        } catch (com.library.books.domain.exception.ReactivationException e) {
            flashDanger(ctx, e.getMessage());
        } catch (com.library.books.domain.exception.LanguageNotFoundException e) {
            flashDanger(ctx, "Language not found: " + id);
        }
        ctx.redirect("/books/languages");
    }
}
