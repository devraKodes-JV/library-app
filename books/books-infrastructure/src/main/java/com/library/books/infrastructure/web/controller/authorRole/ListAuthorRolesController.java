package com.library.books.infrastructure.web.controller.authorRole;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.authorRole.AuthorRoleResponseDTO;
import com.library.books.application.service.authorRole.ListAuthorRolesUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ListAuthorRolesController extends BaseController {

    private final ListAuthorRolesUseCase listAuthorRolesUseCase;

    public ListAuthorRolesController(ListAuthorRolesUseCase listAuthorRolesUseCase, WebControllerContext webContext) {
        super(webContext);
        this.listAuthorRolesUseCase = listAuthorRolesUseCase;
    }

    public void listAuthorRoles(Context ctx) {
        requireCan(ctx, "authorRoles.read");
        List<AuthorRoleResponseDTO> authorRoles = listAuthorRolesUseCase.execute();
        ctx.render("books/authorRoles/list", buildListModel(ctx, Map.of("authorRoles", authorRoles)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        var navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "authorRoles.create"));
        model.put("canUpdate", hasPermission(ctx, "authorRoles.update"));
        model.put("canDelete", hasPermission(ctx, "authorRoles.delete"));
        model.putAll(extra);
        return model;
    }
}
