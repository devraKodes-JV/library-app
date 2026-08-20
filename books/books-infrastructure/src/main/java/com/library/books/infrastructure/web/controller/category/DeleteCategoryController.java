package com.library.books.infrastructure.web.controller.category;

import com.library.books.application.dto.command.category.DeleteCategoryCommand;
import com.library.books.application.service.category.DeleteCategoryUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class DeleteCategoryController extends BaseController {

    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public DeleteCategoryController(DeleteCategoryUseCase deleteCategoryUseCase, WebControllerContext webContext) {
        super(webContext);
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    public void deleteCategory(Context ctx) {
        requireCan(ctx, "categories.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteCategoryUseCase.execute(new DeleteCategoryCommand(id));
        flashWarning(ctx, "Category deleted.");
        ctx.redirect("/books/categories");
    }

}