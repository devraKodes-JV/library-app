package com.library.books.infrastructure.web.controller.publisher;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.publisher.CreatePublisherCommand;
import com.library.books.application.service.publisher.CreatePublisherUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class CreatePublisherController {

    private final CreatePublisherUseCase createPublisherUseCase;
    private final WebControllerContext webContext;

    public CreatePublisherController(CreatePublisherUseCase createPublisherUseCase, WebControllerContext webContext) {
        this.createPublisherUseCase = createPublisherUseCase;
        this.webContext = webContext;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "publishers.create");
        ctx.render("books/publishers/form", buildCreateModel(ctx));
    }

    public void createPublisher(Context ctx) {
        requireCan(ctx, "publishers.create");
        CreatePublisherCommand command = new CreatePublisherCommand(
                ctx.formParam("name"),
                ctx.formParam("country"),
                ctx.formParam("website"));

        try {
            createPublisherUseCase.execute(command);
            WebHelper.flashSuccess(ctx, "Publisher created successfully.");
            ctx.redirect("/books/publishers");
        } catch (ValidationException e) {
            Map<String, Object> model = buildCreateModel(ctx);
            model.putAll(e.getFieldErrors());
            ctx.render("books/publishers/form", model);
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("publisher", null);
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
