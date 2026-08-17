package com.library.books.infrastructure.web.controller.bookFormat;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.bookFormat.CreateBookFormatCommand;
import com.library.books.application.service.bookFormat.CreateBookFormatUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class CreateBookFormatController {

    private final CreateBookFormatUseCase createBookFormatUseCase;
    private final WebControllerContext webContext;

    public CreateBookFormatController(CreateBookFormatUseCase createBookFormatUseCase, WebControllerContext webContext) {
        this.createBookFormatUseCase = createBookFormatUseCase;
        this.webContext = webContext;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "formats.create");
        ctx.render("books/formats/form", buildCreateModel(ctx));
    }

    public void createFormat(Context ctx) {
        requireCan(ctx, "formats.create");
        CreateBookFormatCommand command = new CreateBookFormatCommand(
                ctx.formParam("code"),
                ctx.formParam("name"),
                ctx.formParam("description"));

        try {
            createBookFormatUseCase.execute(command);
            WebHelper.flashSuccess(ctx, "Format created successfully.");
            ctx.redirect("/books/formats");
        } catch (ValidationException e) {
            Map<String, Object> model = buildCreateModel(ctx);
            model.putAll(e.getFieldErrors());
            ctx.render("books/formats/form", model);
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("format", null);
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
