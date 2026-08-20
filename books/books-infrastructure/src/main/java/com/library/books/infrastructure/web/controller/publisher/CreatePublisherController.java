package com.library.books.infrastructure.web.controller.publisher;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.publisher.CreatePublisherCommand;
import com.library.books.application.service.publisher.CreatePublisherUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class CreatePublisherController extends BaseController {

    private final CreatePublisherUseCase createPublisherUseCase;

    public CreatePublisherController(CreatePublisherUseCase createPublisherUseCase, WebControllerContext webContext) {
        super(webContext);
        this.createPublisherUseCase = createPublisherUseCase;
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
            flashSuccess(ctx, "Publisher created successfully.");
            ctx.redirect("/books/publishers");
        } catch (ValidationException e) {
            Map<String, Object> model = buildCreateModel(ctx);
            model.putAll(e.getFieldErrors());
            ctx.render("books/publishers/form", model);
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("publisher", null);
        model.put("user", current);
        model.put("navSections", navSections);
        return model;
    }

}