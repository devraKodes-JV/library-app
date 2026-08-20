package com.library.books.infrastructure.web.controller.author;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.author.UpdateAuthorCommand;
import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.service.author.UpdateAuthorUseCase;
import com.library.books.application.service.author.GetAuthorUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class UpdateAuthorController extends BaseController {

    private final UpdateAuthorUseCase updateAuthorUseCase;
    private final GetAuthorUseCase getAuthorUseCase;

    public UpdateAuthorController(UpdateAuthorUseCase updateAuthorUseCase, GetAuthorUseCase getAuthorUseCase, WebControllerContext webContext) {
        super(webContext);
        this.updateAuthorUseCase = updateAuthorUseCase;
        this.getAuthorUseCase = getAuthorUseCase;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "authors.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        AuthorResponseDTO author = getAuthorUseCase.execute(id);
        ctx.render("books/authors/form", buildEditModel(ctx, Map.of("author", author)));
    }

    public void updateAuthor(Context ctx) {
        requireCan(ctx, "authors.update");
        UpdateAuthorCommand command = new UpdateAuthorCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                ctx.formParam("firstName"),
                ctx.formParam("lastName"),
                ctx.formParam("biography"),
                ctx.formParam("birthDate"),
                ctx.formParam("deathDate"));

        try {
            updateAuthorUseCase.execute(command);
            flashSuccess(ctx, "Author updated successfully.");
            ctx.redirect("/books/authors");
        } catch (ValidationException e) {
            AuthorResponseDTO author = getAuthorUseCase.execute(command.id());
            Map<String, Object> model = buildEditModel(ctx, Map.of("author", author));
            model.putAll(e.getFieldErrors());
            ctx.render("books/authors/form", model);
            return;
        }
    }

    private Map<String, Object> buildEditModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "edit");
        model.put("user", current);
        model.put("navSections", navSections);
        model.putAll(extra);
        return model;
    }

}