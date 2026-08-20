package com.library.books.infrastructure.web.controller.language;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.service.language.ListLanguagesUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ListLanguagesController extends BaseController {

    private final ListLanguagesUseCase listLanguagesUseCase;

    public ListLanguagesController(ListLanguagesUseCase listLanguagesUseCase, WebControllerContext webContext) {
        super(webContext);
        this.listLanguagesUseCase = listLanguagesUseCase;
    }

    public void listLanguages(Context ctx) {
        requireCan(ctx, "languages.read");
        List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
        ctx.render("books/languages/list", buildListModel(ctx, Map.of(
                "languages", languages)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "languages.create"));
        model.put("canUpdate", hasPermission(ctx, "languages.update"));
        model.put("canDelete", hasPermission(ctx, "languages.delete"));
        model.putAll(extra);
        return model;
    }

}