package com.library.books.infrastructure.web.controller.authorRole;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.authorRole.AuthorRoleResponseDTO;
import com.library.books.application.service.authorRole.GetAuthorRoleUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ShowAuthorRoleController extends BaseController {

    private final GetAuthorRoleUseCase getAuthorRoleUseCase;

    public ShowAuthorRoleController(GetAuthorRoleUseCase getAuthorRoleUseCase, WebControllerContext webContext) {
        super(webContext);
        this.getAuthorRoleUseCase = getAuthorRoleUseCase;
    }

    public void showAuthorRole(Context ctx) {
        requireCan(ctx, "authorRoles.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        AuthorRoleResponseDTO authorRole;
        try {
            authorRole = getAuthorRoleUseCase.execute(id);
        } catch (com.library.books.domain.exception.AuthorRoleNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("Author role not found");
        }
        ctx.render("books/authorRoles/show", buildShowModel(ctx, Map.of("authorRole", authorRole)));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", hasPermission(ctx, "authorRoles.update"));
        model.put("canDelete", hasPermission(ctx, "authorRoles.delete"));
        model.putAll(extra);
        return model;
    }
}
