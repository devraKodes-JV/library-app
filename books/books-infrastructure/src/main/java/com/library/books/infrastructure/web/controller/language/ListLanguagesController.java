package com.library.books.infrastructure.web.controller.language;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.service.language.ListLanguagesUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListLanguagesController {

    private final ListLanguagesUseCase listLanguagesUseCase;
    private final WebControllerContext webContext;

    public ListLanguagesController(ListLanguagesUseCase listLanguagesUseCase, WebControllerContext webContext) {
        this.listLanguagesUseCase = listLanguagesUseCase;
        this.webContext = webContext;
    }

    public void listLanguages(Context ctx) {
        requireCan(ctx, "languages.read");
        List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
        ctx.render("books/languages/list", buildListModel(ctx, Map.of(
                "languages", languages)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", webContext.hasPermission(ctx, "languages.create"));
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
