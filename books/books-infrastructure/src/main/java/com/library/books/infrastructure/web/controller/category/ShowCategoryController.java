package com.library.books.infrastructure.web.controller.category;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.service.category.GetCategoryUseCase;
import com.library.books.application.service.category.ListCategoriesUseCase;
import com.library.books.application.service.work.ListWorksByCategoryUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ShowCategoryController {

    private final GetCategoryUseCase getCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final ListWorksByCategoryUseCase listWorksByCategoryUseCase;
    private final WebControllerContext webContext;

    public ShowCategoryController(GetCategoryUseCase getCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase, ListWorksByCategoryUseCase listWorksByCategoryUseCase, WebControllerContext webContext) {
        this.getCategoryUseCase = getCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
        this.listWorksByCategoryUseCase = listWorksByCategoryUseCase;
        this.webContext = webContext;
    }

    public void showCategory(Context ctx) {
        requireCan(ctx, "categories.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        CategoryResponseDTO category;
        try {
            category = getCategoryUseCase.execute(id);
        } catch (com.library.books.domain.exception.CategoryNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("Category not found");
        }
        List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
        var relatedWorks = listWorksByCategoryUseCase.execute(id);
        ctx.render("books/categories/show", buildShowModel(ctx, Map.of(
                "category", category,
                "categories", categories,
                "relatedWorks", relatedWorks)));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
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
