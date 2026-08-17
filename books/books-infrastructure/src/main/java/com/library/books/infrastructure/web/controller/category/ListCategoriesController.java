package com.library.books.infrastructure.web.controller.category;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.service.category.ListCategoriesUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListCategoriesController {

    private final ListCategoriesUseCase listCategoriesUseCase;
    private final WebControllerContext webContext;

    public ListCategoriesController(ListCategoriesUseCase listCategoriesUseCase, WebControllerContext webContext) {
        this.listCategoriesUseCase = listCategoriesUseCase;
        this.webContext = webContext;
    }

    public void listCategories(Context ctx) {
        requireCan(ctx, "categories.read");
        List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
        ctx.render("books/categories/list", buildListModel(ctx, Map.of(
                "categories", categories)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", webContext.hasPermission(ctx, "categories.create"));
        model.put("canUpdate", webContext.hasPermission(ctx, "categories.update"));
        model.put("canDelete", webContext.hasPermission(ctx, "categories.delete"));
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
