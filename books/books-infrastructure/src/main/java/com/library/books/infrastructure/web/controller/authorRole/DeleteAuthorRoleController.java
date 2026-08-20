package com.library.books.infrastructure.web.controller.authorRole;

import com.library.books.application.service.authorRole.DeleteAuthorRoleUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class DeleteAuthorRoleController extends BaseController {

    private final DeleteAuthorRoleUseCase deleteAuthorRoleUseCase;

    public DeleteAuthorRoleController(DeleteAuthorRoleUseCase deleteAuthorRoleUseCase, WebControllerContext webContext) {
        super(webContext);
        this.deleteAuthorRoleUseCase = deleteAuthorRoleUseCase;
    }

    public void deleteAuthorRole(Context ctx) {
        requireCan(ctx, "authorRoles.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteAuthorRoleUseCase.execute(id);
        flashSuccess(ctx, "Author role deleted successfully.");
        ctx.redirect("/books/authorRoles");
    }
}
