package com.library.books.infrastructure.web.controller.author;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.author.CreateAuthorCommand;
import com.library.books.application.service.author.CreateAuthorUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class CreateAuthorController extends BaseController {

    private final CreateAuthorUseCase createAuthorUseCase;

    public CreateAuthorController(CreateAuthorUseCase createAuthorUseCase, WebControllerContext webContext) {
        super(webContext);
        this.createAuthorUseCase = createAuthorUseCase;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "authors.create");
        ctx.render("books/authors/form", buildCreateModel(ctx));
    }

    public void createAuthor(Context ctx) {
        requireCan(ctx, "authors.create");
        CreateAuthorCommand command = new CreateAuthorCommand(
                ctx.formParam("firstName"),
                ctx.formParam("lastName"),
                ctx.formParam("biography"),
                ctx.formParam("birthDate"),
                ctx.formParam("deathDate"));

        try {
            createAuthorUseCase.execute(command);
            flashSuccess(ctx, "Author created successfully.");
            ctx.redirect("/books/authors");
        } catch (ValidationException e) {
            Map<String, Object> model = buildCreateModel(ctx);
            model.putAll(e.getFieldErrors());
            ctx.render("books/authors/form", model);
            return;
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("author", null);
        model.put("user", current);
        model.put("navSections", navSections);
        return model;
    }

}