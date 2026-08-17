package com.library.books.infrastructure.web.controller.category;

import com.library.books.application.dto.command.category.DeleteCategoryCommand;
import com.library.books.application.service.category.DeleteCategoryUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class DeleteCategoryController {

    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final WebControllerContext webContext;

    public DeleteCategoryController(DeleteCategoryUseCase deleteCategoryUseCase, WebControllerContext webContext) {
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.webContext = webContext;
    }

    public void deleteCategory(Context ctx) {
        requireCan(ctx, "categories.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteCategoryUseCase.execute(new DeleteCategoryCommand(id));
        WebHelper.flashWarning(ctx, "Category deleted.");
        ctx.redirect("/books/categories");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
