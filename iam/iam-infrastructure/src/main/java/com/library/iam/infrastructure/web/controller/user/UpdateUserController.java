package com.library.iam.infrastructure.web.controller.user;

import java.util.List;
import java.util.Map;

import com.library.iam.application.dto.UserDTO;
import com.library.iam.application.dto.command.user.UpdateUserCommand;
import com.library.iam.application.service.user.GetUserUseCase;
import com.library.iam.application.service.user.UpdateUserUseCase;
import com.library.iam.application.service.role.ListRolesUseCase;
import com.library.iam.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class UpdateUserController {

    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final ListRolesUseCase listRolesUseCase;
    private final WebControllerContext webContext;

    public UpdateUserController(GetUserUseCase getUserUseCase,
                                UpdateUserUseCase updateUserUseCase,
                                ListRolesUseCase listRolesUseCase,
                                WebControllerContext webContext) {
        this.getUserUseCase = getUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.listRolesUseCase = listRolesUseCase;
        this.webContext = webContext;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "users.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var current = webContext.currentUser(ctx);
        UserDTO target = getUserUseCase.execute(id);
        List<?> roles = listRolesUseCase.execute();
        List<?> sections = webContext.navSections(ctx);

        ctx.render("users/form", Map.of(
                "user", current,
                "navSections", sections,
                "mode", "edit",
                "userForm", target,
                "roles", roles));
    }

    public void updateUser(Context ctx) {
        requireCan(ctx, "users.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        UpdateUserCommand command = new UpdateUserCommand(
                id,
                ctx.formParam("fullName"),
                ctx.formParam("email"),
                "on".equals(ctx.formParam("enabled")),
                ctx.formParam("roleName"));

        try {
            updateUserUseCase.execute(command);
            WebHelper.flashSuccess(ctx, "User updated successfully.");
            ctx.redirect("/iam/users");
        } catch (ValidationException e) {
            var current = webContext.currentUser(ctx);
            UserDTO target = getUserUseCase.execute(id);
            List<?> roles = listRolesUseCase.execute();
            List<?> sections = webContext.navSections(ctx);

            Map<String, Object> model = new java.util.LinkedHashMap<>();
            model.put("user", current);
            model.put("navSections", sections);
            model.put("mode", "edit");
            model.put("userForm", target);
            model.put("roles", roles);
            model.put("validationError", true);
            model.putAll(e.getFieldErrors());
            ctx.render("users/form", model);
            return;
        }
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
