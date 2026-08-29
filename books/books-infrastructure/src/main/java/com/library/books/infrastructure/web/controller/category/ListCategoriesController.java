package com.library.books.infrastructure.web.controller.category;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.service.category.ListCategoriesUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ListCategoriesController extends BaseController {

    private final ListCategoriesUseCase listCategoriesUseCase;

    public ListCategoriesController(ListCategoriesUseCase listCategoriesUseCase, WebControllerContext webContext) {
        super(webContext);
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    public void listCategories(Context ctx) {
        requireCan(ctx, "categories.read");
        String status = ctx.queryParamAsClass("status", String.class).getOrDefault("active");
        List<CategoryResponseDTO> categories = listCategoriesUseCase.execute(status);
        ctx.render("books/categories/list", buildListModel(ctx, Map.of(
                "categories", categories,
                "status", status)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "categories.create"));
        model.put("canUpdate", hasPermission(ctx, "categories.update"));
        model.put("canDelete", hasPermission(ctx, "categories.delete"));
        model.put("canReactivate", hasPermission(ctx, "categories.reactivate"));
        model.putAll(extra);
        return model;
    }

}