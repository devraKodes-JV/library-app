package com.library.books.infrastructure.web.controller.language;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.language.CreateLanguageCommand;
import com.library.books.application.service.language.CreateLanguageUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class CreateLanguageController {

    private final CreateLanguageUseCase createLanguageUseCase;
    private final WebControllerContext webContext;

    public CreateLanguageController(CreateLanguageUseCase createLanguageUseCase, WebControllerContext webContext) {
        this.createLanguageUseCase = createLanguageUseCase;
        this.webContext = webContext;
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
            WebHelper.flashSuccess(ctx, "Language created successfully.");
            ctx.redirect("/books/languages");
        } catch (ValidationException e) {
            Map<String, Object> model = buildCreateModel(ctx);
            model.putAll(e.getFieldErrors());
            ctx.render("books/languages/form", model);
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("language", null);
        model.put("user", current);
        model.put("navSections", navSections);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
