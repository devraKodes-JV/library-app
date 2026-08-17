package com.library.iam.infrastructure.web.controller.user;

import java.util.List;
import java.util.Map;

import com.library.iam.application.dto.command.user.CreateUserCommand;
import com.library.iam.application.service.role.ListRolesUseCase;
import com.library.iam.application.service.user.CreateUserUseCase;
import com.library.iam.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class CreateUserController {

    private final CreateUserUseCase createUserUseCase;
    private final ListRolesUseCase listRolesUseCase;
    private final WebControllerContext webContext;

    public CreateUserController(CreateUserUseCase createUserUseCase,
                                ListRolesUseCase listRolesUseCase,
                                WebControllerContext webContext) {
        this.createUserUseCase = createUserUseCase;
        this.listRolesUseCase = listRolesUseCase;
        this.webContext = webContext;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "users.create");
        var current = webContext.currentUser(ctx);
        List<?> roles = listRolesUseCase.execute();
        List<?> sections = webContext.navSections(ctx);

        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", sections);
        model.put("mode", "create");
        model.put("userForm", null);
        model.put("roles", roles);
        ctx.render("users/form", model);
    }

    public void createUser(Context ctx) {
        requireCan(ctx, "users.create");
        CreateUserCommand command = new CreateUserCommand(
                ctx.formParam("username"),
                ctx.formParam("password"),
                ctx.formParam("fullName"),
                ctx.formParam("email"),
                "on".equals(ctx.formParam("enabled")),
                ctx.formParam("roleName"));

        try {
            createUserUseCase.execute(command);
            WebHelper.flashSuccess(ctx, "User created successfully.");
            ctx.redirect("/iam/users");
        } catch (ValidationException e) {
            var current = webContext.currentUser(ctx);
            List<?> roles = listRolesUseCase.execute();
            List<?> sections = webContext.navSections(ctx);

            Map<String, Object> model = new java.util.LinkedHashMap<>();
            model.put("user", current);
            model.put("navSections", sections);
            model.put("mode", "create");
            model.put("userForm", null);
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
