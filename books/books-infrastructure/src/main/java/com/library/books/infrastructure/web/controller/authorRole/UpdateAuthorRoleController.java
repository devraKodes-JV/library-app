package com.library.books.infrastructure.web.controller.authorRole;

import java.util.Map;

import com.library.books.application.dto.command.authorRole.UpdateAuthorRoleCommand;
import com.library.books.application.dto.response.authorRole.AuthorRoleResponseDTO;
import com.library.books.application.service.authorRole.GetAuthorRoleUseCase;
import com.library.books.application.service.authorRole.UpdateAuthorRoleUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class UpdateAuthorRoleController extends BaseController {

    private final UpdateAuthorRoleUseCase updateAuthorRoleUseCase;
    private final GetAuthorRoleUseCase getAuthorRoleUseCase;

    public UpdateAuthorRoleController(UpdateAuthorRoleUseCase updateAuthorRoleUseCase, GetAuthorRoleUseCase getAuthorRoleUseCase, WebControllerContext webContext) {
        super(webContext);
        this.updateAuthorRoleUseCase = updateAuthorRoleUseCase;
        this.getAuthorRoleUseCase = getAuthorRoleUseCase;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "authorRoles.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        AuthorRoleResponseDTO authorRole = getAuthorRoleUseCase.execute(id);
        ctx.render("books/authorRoles/form", buildEditModel(ctx, Map.of("authorRole", authorRole)));
    }

    public void updateAuthorRole(Context ctx) {
        requireCan(ctx, "authorRoles.update");
        UpdateAuthorRoleCommand command = new UpdateAuthorRoleCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                ctx.formParam("code"),
                ctx.formParam("name"),
                ctx.formParam("description"));

        try {
            updateAuthorRoleUseCase.execute(command);
            flashSuccess(ctx, "Author role updated successfully.");
            ctx.redirect("/books/authorRoles");
        } catch (ValidationException e) {
            AuthorRoleResponseDTO authorRole = getAuthorRoleUseCase.execute(command.id());
            Map<String, Object> model = buildEditModel(ctx, Map.of("authorRole", authorRole));
            model.put("validationError", true);
            model.putAll(e.getFieldErrors());
            ctx.render("books/authorRoles/form", model);
            return;
        }
    }

    private Map<String, Object> buildEditModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        var navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "edit");
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "authorRoles.create"));
        model.put("canUpdate", hasPermission(ctx, "authorRoles.update"));
        model.put("canDelete", hasPermission(ctx, "authorRoles.delete"));
        model.putAll(extra);
        return model;
    }
}
