package com.library.iam.infrastructure.web.controller.role;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.library.iam.application.dto.PermissionDTO;
import com.library.iam.application.dto.PermissionGroup;
import com.library.iam.application.dto.RoleDetailDTO;
import com.library.iam.application.dto.command.role.UpdateRoleCommand;
import com.library.iam.application.service.permission.ListPermissionsGroupedByModuleUseCase;
import com.library.iam.application.service.role.GetRoleDetailUseCase;
import com.library.iam.application.service.role.UpdateRoleUseCase;
import com.library.iam.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class UpdateRoleController {

    private final GetRoleDetailUseCase getRoleDetailUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final ListPermissionsGroupedByModuleUseCase listPermissionsGroupedByModuleUseCase;
    private final WebControllerContext webContext;

    public UpdateRoleController(GetRoleDetailUseCase getRoleDetailUseCase,
                                UpdateRoleUseCase updateRoleUseCase,
                                ListPermissionsGroupedByModuleUseCase listPermissionsGroupedByModuleUseCase,
                                WebControllerContext webContext) {
        this.getRoleDetailUseCase = getRoleDetailUseCase;
        this.updateRoleUseCase = updateRoleUseCase;
        this.listPermissionsGroupedByModuleUseCase = listPermissionsGroupedByModuleUseCase;
        this.webContext = webContext;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "roles.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var current = webContext.currentUser(ctx);
        RoleDetailDTO role = getRoleDetailUseCase.execute(id);
        List<PermissionGroup> groups = listPermissionsGroupedByModuleUseCase.execute();
        List<?> sections = webContext.navSections(ctx);

        Set<Long> selected = role.permissions().stream()
                .map(PermissionDTO::id)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        ctx.render("roles/form", Map.of(
                "mode", "edit",
                "role", role,
                "permissionGroups", groups,
                "selectedPermissionIds", selected,
                "user", current,
                "navSections", sections));
    }

    public void updateRole(Context ctx) {
        requireCan(ctx, "roles.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        UpdateRoleCommand command = new UpdateRoleCommand(
                id,
                ctx.formParam("name"),
                ctx.formParam("description"),
                parseFormParams(ctx.formParams("permissionIds")));

        try {
            updateRoleUseCase.execute(command);
            WebHelper.flashSuccess(ctx, "Role updated successfully.");
            ctx.redirect("/iam/roles");
        } catch (ValidationException e) {
            var current = webContext.currentUser(ctx);
            RoleDetailDTO role = getRoleDetailUseCase.execute(id);
            List<PermissionGroup> groups = listPermissionsGroupedByModuleUseCase.execute();
            List<?> sections = webContext.navSections(ctx);

            Set<Long> selected = role.permissions().stream()
                    .map(PermissionDTO::id)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toCollection(LinkedHashSet::new));

            Map<String, Object> model = new java.util.LinkedHashMap<>();
            model.put("mode", "edit");
            model.put("role", role);
            model.put("permissionGroups", groups);
            model.put("selectedPermissionIds", selected);
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
