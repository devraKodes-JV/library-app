package com.library.books.infrastructure.web.controller.category;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.category.UpdateCategoryCommand;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.service.category.UpdateCategoryUseCase;
import com.library.books.application.service.category.GetCategoryUseCase;
import com.library.books.application.service.category.ListCategoriesUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class UpdateCategoryController extends BaseController {

    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public UpdateCategoryController(UpdateCategoryUseCase updateCategoryUseCase, GetCategoryUseCase getCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase, WebControllerContext webContext) {
        super(webContext);
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "categories.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        CategoryResponseDTO category = getCategoryUseCase.execute(id);
        List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
        ctx.render("books/categories/form", buildEditModel(ctx, Map.of(
                "category", category,
                "categories", categories)));
    }

    public void updateCategory(Context ctx) {
        requireCan(ctx, "categories.update");
        UpdateCategoryCommand command = new UpdateCategoryCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                ctx.formParam("code"),
                ctx.formParam("name"),
                ctx.formParam("description"),
                parseLong(ctx.formParam("parentId")));

        try {
            updateCategoryUseCase.execute(command);
            flashSuccess(ctx, "Category updated successfully.");
            ctx.redirect("/books/categories");
        } catch (ValidationException e) {
            CategoryResponseDTO category = getCategoryUseCase.execute(command.id());
            List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
            Map<String, Object> model = buildEditModel(ctx, Map.of(
                    "category", category,
                    "categories", categories));
            model.putAll(e.getFieldErrors());
            ctx.render("books/categories/form", model);
        }
    }

    private Map<String, Object> buildEditModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "edit");
        model.put("user", current);
        model.put("navSections", navSections);
        model.putAll(extra);
        return model;
    }


    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}