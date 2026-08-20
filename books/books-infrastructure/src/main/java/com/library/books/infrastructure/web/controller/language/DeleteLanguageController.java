package com.library.books.infrastructure.web.controller.language;

import com.library.books.application.dto.command.language.DeleteLanguageCommand;
import com.library.books.application.service.language.DeleteLanguageUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class DeleteLanguageController extends BaseController {

    private final DeleteLanguageUseCase deleteLanguageUseCase;

    public DeleteLanguageController(DeleteLanguageUseCase deleteLanguageUseCase, WebControllerContext webContext) {
        super(webContext);
        this.deleteLanguageUseCase = deleteLanguageUseCase;
    }

    public void deleteLanguage(Context ctx) {
        requireCan(ctx, "languages.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteLanguageUseCase.execute(new DeleteLanguageCommand(id));
        flashWarning(ctx, "Language deleted.");
        ctx.redirect("/books/languages");
    }

}