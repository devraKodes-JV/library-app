package com.library.iam.infrastructure.web.controller.role;

import java.util.List;
import java.util.Map;

import com.library.iam.application.dto.RoleDTO;
import com.library.iam.application.service.role.ListRolesUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListRolesController {

    private final ListRolesUseCase listRolesUseCase;
    private final WebControllerContext webContext;

    public ListRolesController(ListRolesUseCase listRolesUseCase,
                               WebControllerContext webContext) {
        this.listRolesUseCase = listRolesUseCase;
        this.webContext = webContext;
    }

    public void listRoles(Context ctx) {
        requireCan(ctx, "roles.read");
        var current = webContext.currentUser(ctx);
        String status = ctx.queryParamAsClass("status", String.class).getOrDefault("active");
        var roles = listRolesUseCase.execute(status);
        List<?> sections = webContext.navSections(ctx);

        ctx.render("roles/list", Map.of(
                "roles", roles,
                "user", current,
                "navSections", sections,
                "status", status,
                "canCreate", webContext.hasPermission(ctx, "roles.create"),
                "canUpdate", webContext.hasPermission(ctx, "roles.update"),
                "canDelete", webContext.hasPermission(ctx, "roles.delete"),
                "canReinstate", webContext.hasPermission(ctx, "roles.reinstate")));
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
