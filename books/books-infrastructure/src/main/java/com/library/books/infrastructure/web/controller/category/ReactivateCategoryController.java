package com.library.books.infrastructure.web.controller.category;

import com.library.books.application.dto.command.category.ReactivateCategoryCommand;
import com.library.books.application.service.category.ReactivateCategoryUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ReactivateCategoryController extends BaseController {

    private final ReactivateCategoryUseCase reactivateCategoryUseCase;

    public ReactivateCategoryController(ReactivateCategoryUseCase reactivateCategoryUseCase, WebControllerContext webContext) {
        super(webContext);
        this.reactivateCategoryUseCase = reactivateCategoryUseCase;
    }

    public void reactivateCategory(Context ctx) {
        requireCan(ctx, "categories.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivateCategoryUseCase.execute(new ReactivateCategoryCommand(id));
            flashSuccess(ctx, "Category reactivated.");
        } catch (com.library.books.domain.exception.ReactivationException e) {
            flashDanger(ctx, e.getMessage());
        } catch (com.library.books.domain.exception.CategoryNotFoundException e) {
            flashDanger(ctx, "Category not found: " + id);
        }
        ctx.redirect("/books/categories");
    }
}
