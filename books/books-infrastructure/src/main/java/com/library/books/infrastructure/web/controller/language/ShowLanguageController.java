package com.library.books.infrastructure.web.controller.language;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.service.language.GetLanguageUseCase;
import com.library.books.application.service.work.ListWorksByLanguageUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ShowLanguageController extends BaseController {

    private final GetLanguageUseCase getLanguageUseCase;
    private final ListWorksByLanguageUseCase listWorksByLanguageUseCase;

    public ShowLanguageController(GetLanguageUseCase getLanguageUseCase, ListWorksByLanguageUseCase listWorksByLanguageUseCase, WebControllerContext webContext) {
        super(webContext);
        this.getLanguageUseCase = getLanguageUseCase;
        this.listWorksByLanguageUseCase = listWorksByLanguageUseCase;
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
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", hasPermission(ctx, "languages.update"));
        model.put("canDelete", hasPermission(ctx, "languages.delete"));
        model.putAll(extra);
        return model;
    }

}