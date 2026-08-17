package com.library.books.infrastructure.web.controller.language;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.service.language.GetLanguageUseCase;
import com.library.books.application.service.work.ListWorksByLanguageUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ShowLanguageController {

    private final GetLanguageUseCase getLanguageUseCase;
    private final ListWorksByLanguageUseCase listWorksByLanguageUseCase;
    private final WebControllerContext webContext;

    public ShowLanguageController(GetLanguageUseCase getLanguageUseCase, ListWorksByLanguageUseCase listWorksByLanguageUseCase, WebControllerContext webContext) {
        this.getLanguageUseCase = getLanguageUseCase;
        this.listWorksByLanguageUseCase = listWorksByLanguageUseCase;
        this.webContext = webContext;
    }

    public void showLanguage(Context ctx) {
        requireCan(ctx, "languages.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        LanguageResponseDTO language;
        try {
            language = getLanguageUseCase.execute(id);
        } catch (com.library.books.domain.exception.LanguageNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("Language not found");
        }
        var relatedWorks = listWorksByLanguageUseCase.execute(id);
        ctx.render("books/languages/show", buildShowModel(ctx, Map.of(
                "language", language,
                "relatedWorks", relatedWorks)));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", webContext.hasPermission(ctx, "languages.update"));
        model.put("canDelete", webContext.hasPermission(ctx, "languages.delete"));
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
