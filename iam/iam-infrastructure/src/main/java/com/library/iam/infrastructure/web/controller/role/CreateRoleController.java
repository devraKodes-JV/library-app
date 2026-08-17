package com.library.iam.infrastructure.web.controller.role;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.library.iam.application.dto.PermissionGroup;
import com.library.iam.application.dto.command.role.CreateRoleCommand;
import com.library.iam.application.service.permission.ListPermissionsGroupedByModuleUseCase;
import com.library.iam.application.service.role.CreateRoleUseCase;
import com.library.iam.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class CreateRoleController {

    private final CreateRoleUseCase createRoleUseCase;
    private final ListPermissionsGroupedByModuleUseCase listPermissionsGroupedByModuleUseCase;
    private final WebControllerContext webContext;

    public CreateRoleController(CreateRoleUseCase createRoleUseCase,
                                ListPermissionsGroupedByModuleUseCase listPermissionsGroupedByModuleUseCase,
                                WebControllerContext webContext) {
        this.createRoleUseCase = createRoleUseCase;
        this.listPermissionsGroupedByModuleUseCase = listPermissionsGroupedByModuleUseCase;
        this.webContext = webContext;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "roles.create");
        var current = webContext.currentUser(ctx);
        List<PermissionGroup> groups = listPermissionsGroupedByModuleUseCase.execute();
        List<?> sections = webContext.navSections(ctx);

        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("role", null);
        model.put("permissionGroups", groups);
        model.put("selectedPermissionIds", Set.of());
        model.put("user", current);
        model.put("navSections", sections);
        ctx.render("roles/form", model);
    }

    public void createRole(Context ctx) {
        requireCan(ctx, "roles.create");
        CreateRoleCommand command = new CreateRoleCommand(
                ctx.formParam("name"),
                ctx.formParam("description"),
                parseFormParams(ctx.formParams("permissionIds")));

        try {
            createRoleUseCase.execute(command);
            WebHelper.flashSuccess(ctx, "Role created successfully.");
            ctx.redirect("/iam/roles");
        } catch (ValidationException e) {
            var current = webContext.currentUser(ctx);
            List<PermissionGroup> groups = listPermissionsGroupedByModuleUseCase.execute();
            List<?> sections = webContext.navSections(ctx);

            Map<String, Object> model = new java.util.LinkedHashMap<>();
            model.put("mode", "create");
            model.put("role", null);
            model.put("permissionGroups", groups);
            model.put("selectedPermissionIds", Set.of());
            model.put("user", current);
            model.put("navSections", sections);
            model.put("validationError", true);
            model.putAll(e.getFieldErrors());
            ctx.render("roles/form", model);
            return;
        }
    }

    private static List<Long> parseFormParams(List<String> raw) {
        if (raw == null) {
            return List.of();
        }
        return raw.stream()
                .filter(s -> s != null && !s.isBlank())
                .map(s -> {
                    try {
                        return Long.parseLong(s.trim());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
