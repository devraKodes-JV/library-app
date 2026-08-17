package com.library.books.infrastructure.web.controller.language;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.language.UpdateLanguageCommand;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.service.language.UpdateLanguageUseCase;
import com.library.books.application.service.language.GetLanguageUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class UpdateLanguageController {

    private final UpdateLanguageUseCase updateLanguageUseCase;
    private final GetLanguageUseCase getLanguageUseCase;
    private final WebControllerContext webContext;

    public UpdateLanguageController(UpdateLanguageUseCase updateLanguageUseCase, GetLanguageUseCase getLanguageUseCase, WebControllerContext webContext) {
        this.updateLanguageUseCase = updateLanguageUseCase;
        this.getLanguageUseCase = getLanguageUseCase;
        this.webContext = webContext;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "languages.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        LanguageResponseDTO language = getLanguageUseCase.execute(id);
        ctx.render("books/languages/form", buildEditModel(ctx, Map.of("language", language)));
    }

    public void updateLanguage(Context ctx) {
        requireCan(ctx, "languages.update");
        UpdateLanguageCommand command = new UpdateLanguageCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                ctx.formParam("code"),
                ctx.formParam("name"));

        try {
            updateLanguageUseCase.execute(command);
        } catch (ValidationException e) {
            LanguageResponseDTO language = getLanguageUseCase.execute(command.id());
            Map<String, Object> model = buildEditModel(ctx, Map.of("language", language));
            model.putAll(e.getFieldErrors());
            ctx.render("books/languages/form", model);
            return;
        }

        WebHelper.flashSuccess(ctx, "Language updated successfully.");
        ctx.redirect("/books/languages");
    }

    private Map<String, Object> buildEditModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "edit");
        model.put("user", current);
        model.put("navSections", navSections);
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
