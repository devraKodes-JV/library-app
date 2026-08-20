package com.library.books.infrastructure.web.controller.authorRole;

import java.util.Map;

import com.library.books.application.dto.command.authorRole.CreateAuthorRoleCommand;
import com.library.books.application.service.authorRole.CreateAuthorRoleUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class CreateAuthorRoleController extends BaseController {

    private final CreateAuthorRoleUseCase createAuthorRoleUseCase;

    public CreateAuthorRoleController(CreateAuthorRoleUseCase createAuthorRoleUseCase, WebControllerContext webContext) {
        super(webContext);
        this.createAuthorRoleUseCase = createAuthorRoleUseCase;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "authorRoles.create");
        ctx.render("books/authorRoles/form", buildCreateModel(ctx, Map.of()));
    }

    public void createAuthorRole(Context ctx) {
        requireCan(ctx, "authorRoles.create");
        CreateAuthorRoleCommand command = new CreateAuthorRoleCommand(
                ctx.formParam("code"),
                ctx.formParam("name"),
                ctx.formParam("description"));

        try {
            createAuthorRoleUseCase.execute(command);
            flashSuccess(ctx, "Author role created successfully.");
            ctx.redirect("/books/authorRoles");
        } catch (ValidationException e) {
            Map<String, Object> model = buildCreateModel(ctx, Map.of());
            model.put("validationError", true);
            model.putAll(e.getFieldErrors());
            ctx.render("books/authorRoles/form", model);
            return;
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        var navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("authorRole", null);
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "authorRoles.create"));
        model.put("canUpdate", hasPermission(ctx, "authorRoles.update"));
        model.put("canDelete", hasPermission(ctx, "authorRoles.delete"));
        model.putAll(extra);
        return model;
    }
}
