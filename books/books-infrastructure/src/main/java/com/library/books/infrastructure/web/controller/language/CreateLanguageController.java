package com.library.books.infrastructure.web.controller.language;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.language.CreateLanguageCommand;
import com.library.books.application.service.language.CreateLanguageUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class CreateLanguageController extends BaseController {

    private final CreateLanguageUseCase createLanguageUseCase;

    public CreateLanguageController(CreateLanguageUseCase createLanguageUseCase, WebControllerContext webContext) {
        super(webContext);
        this.createLanguageUseCase = createLanguageUseCase;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "languages.create");
        ctx.render("books/languages/form", buildCreateModel(ctx));
    }

    public void createLanguage(Context ctx) {
        requireCan(ctx, "languages.create");
        CreateLanguageCommand command = new CreateLanguageCommand(
                ctx.formParam("code"),
                ctx.formParam("name"));

        try {
            createLanguageUseCase.execute(command);
            flashSuccess(ctx, "Language created successfully.");
            ctx.redirect("/books/languages");
        } catch (ValidationException e) {
            Map<String, Object> model = buildCreateModel(ctx);
            model.putAll(e.getFieldErrors());
            ctx.render("books/languages/form", model);
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("language", null);
        model.put("user", current);
        model.put("navSections", navSections);
        return model;
    }

}