package com.library.books.infrastructure.web.controller.authorRole;

import com.library.books.application.dto.command.authorRole.ReactivateAuthorRoleCommand;
import com.library.books.application.service.authorRole.ReactivateAuthorRoleUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ReactivateAuthorRoleController extends BaseController {

    private final ReactivateAuthorRoleUseCase reactivateAuthorRoleUseCase;

    public ReactivateAuthorRoleController(ReactivateAuthorRoleUseCase reactivateAuthorRoleUseCase, WebControllerContext webContext) {
        super(webContext);
        this.reactivateAuthorRoleUseCase = reactivateAuthorRoleUseCase;
    }

    public void reactivateAuthorRole(Context ctx) {
        requireCan(ctx, "author_roles.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivateAuthorRoleUseCase.execute(new ReactivateAuthorRoleCommand(id));
            flashSuccess(ctx, "Author role reactivated.");
        } catch (com.library.books.domain.exception.ReactivationException e) {
            flashDanger(ctx, e.getMessage());
        } catch (com.library.books.domain.exception.AuthorRoleNotFoundException e) {
            flashDanger(ctx, "Author role not found: " + id);
        }
        ctx.redirect("/books/author-roles");
    }
}
