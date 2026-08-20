package com.library.books.infrastructure.web.controller.bookFormat;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.bookFormat.CreateBookFormatCommand;
import com.library.books.application.service.bookFormat.CreateBookFormatUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class CreateBookFormatController extends BaseController {

    private final CreateBookFormatUseCase createBookFormatUseCase;

    public CreateBookFormatController(CreateBookFormatUseCase createBookFormatUseCase, WebControllerContext webContext) {
        super(webContext);
        this.createBookFormatUseCase = createBookFormatUseCase;
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
            flashSuccess(ctx, "Format created successfully.");
            ctx.redirect("/books/formats");
        } catch (ValidationException e) {
            Map<String, Object> model = buildCreateModel(ctx);
            model.putAll(e.getFieldErrors());
            ctx.render("books/formats/form", model);
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("format", null);
        model.put("user", current);
        model.put("navSections", navSections);
        return model;
    }

}