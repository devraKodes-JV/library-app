package com.library.books.infrastructure.web.controller.category;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.category.CreateCategoryCommand;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.service.category.CreateCategoryUseCase;
import com.library.books.application.service.category.ListCategoriesUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class CreateCategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final WebControllerContext webContext;

    public CreateCategoryController(CreateCategoryUseCase createCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase, WebControllerContext webContext) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
        this.webContext = webContext;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "categories.create");
        List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
        ctx.render("books/categories/form", buildCreateModel(ctx, Map.of("categories", categories)));
    }

    public void createCategory(Context ctx) {
        requireCan(ctx, "categories.create");
        CreateCategoryCommand command = new CreateCategoryCommand(
                ctx.formParam("code"),
                ctx.formParam("name"),
                ctx.formParam("description"),
                parseLong(ctx.formParam("parentId")));

        try {
            createCategoryUseCase.execute(command);
            WebHelper.flashSuccess(ctx, "Category created successfully.");
            ctx.redirect("/books/categories");
        } catch (ValidationException e) {
            List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
            Map<String, Object> model = buildCreateModel(ctx, Map.of("categories", categories));
            model.putAll(e.getFieldErrors());
            ctx.render("books/categories/form", model);
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("category", null);
        model.put("user", current);
        model.put("navSections", navSections);
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
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
