package com.library.books.infrastructure.web.controller.language;

import com.library.books.application.dto.command.language.DeleteLanguageCommand;
import com.library.books.application.service.language.DeleteLanguageUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class DeleteLanguageController {

    private final DeleteLanguageUseCase deleteLanguageUseCase;
    private final WebControllerContext webContext;

    public DeleteLanguageController(DeleteLanguageUseCase deleteLanguageUseCase, WebControllerContext webContext) {
        this.deleteLanguageUseCase = deleteLanguageUseCase;
        this.webContext = webContext;
    }

    public void deleteLanguage(Context ctx) {
        requireCan(ctx, "languages.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteLanguageUseCase.execute(new DeleteLanguageCommand(id));
        WebHelper.flashWarning(ctx, "Language deleted.");
        ctx.redirect("/books/languages");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
