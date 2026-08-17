package com.library.books.infrastructure.web.controller.edition;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.edition.ListEditionsUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListEditionsController {

    private final ListEditionsUseCase listEditionsUseCase;
    private final WebControllerContext webContext;

    public ListEditionsController(ListEditionsUseCase listEditionsUseCase, WebControllerContext webContext) {
        this.listEditionsUseCase = listEditionsUseCase;
        this.webContext = webContext;
    }

    public void listEditions(Context ctx) {
        requireCan(ctx, "editions.read");
        List<EditionResponseDTO> editions = listEditionsUseCase.execute();
        ctx.render("books/editions/list", buildListModel(ctx, Map.of(
                "editions", editions)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", webContext.hasPermission(ctx, "editions.create"));
        model.put("canUpdate", webContext.hasPermission(ctx, "editions.update"));
        model.put("canDelete", webContext.hasPermission(ctx, "editions.delete"));
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
