package com.library.iam.infrastructure.web.controller.permission;

import java.util.List;
import java.util.Map;

import com.library.iam.application.dto.PermissionDTO;
import com.library.iam.application.service.permission.ListPermissionsFlatUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListPermissionsController {

    private final ListPermissionsFlatUseCase listPermissionsFlatUseCase;
    private final WebControllerContext webContext;

    public ListPermissionsController(ListPermissionsFlatUseCase listPermissionsFlatUseCase,
                                     WebControllerContext webContext) {
        this.listPermissionsFlatUseCase = listPermissionsFlatUseCase;
        this.webContext = webContext;
    }

    public void listPermissions(Context ctx) {
        requireCan(ctx, "permissions.read");
        var current = webContext.currentUser(ctx);
        List<PermissionDTO> permissions = listPermissionsFlatUseCase.execute();
        List<?> sections = webContext.navSections(ctx);

        ctx.render("permissions/list", Map.of(
                "user", current,
                "navSections", sections,
                "permissions", permissions));
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
