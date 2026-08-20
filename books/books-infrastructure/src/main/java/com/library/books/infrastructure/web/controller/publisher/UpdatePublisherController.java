package com.library.books.infrastructure.web.controller.publisher;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.publisher.UpdatePublisherCommand;
import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.service.publisher.UpdatePublisherUseCase;
import com.library.books.application.service.publisher.GetPublisherUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class UpdatePublisherController extends BaseController {

    private final UpdatePublisherUseCase updatePublisherUseCase;
    private final GetPublisherUseCase getPublisherUseCase;

    public UpdatePublisherController(UpdatePublisherUseCase updatePublisherUseCase, GetPublisherUseCase getPublisherUseCase, WebControllerContext webContext) {
        super(webContext);
        this.updatePublisherUseCase = updatePublisherUseCase;
        this.getPublisherUseCase = getPublisherUseCase;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "publishers.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        PublisherResponseDTO publisher = getPublisherUseCase.execute(id);
        ctx.render("books/publishers/form", buildEditModel(ctx, Map.of("publisher", publisher)));
    }

    public void updatePublisher(Context ctx) {
        requireCan(ctx, "publishers.update");
        UpdatePublisherCommand command = new UpdatePublisherCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                ctx.formParam("name"),
                ctx.formParam("country"),
                ctx.formParam("website"));

        try {
            updatePublisherUseCase.execute(command);
        } catch (ValidationException e) {
            PublisherResponseDTO publisher = getPublisherUseCase.execute(command.id());
            Map<String, Object> model = buildEditModel(ctx, Map.of("publisher", publisher));
            model.putAll(e.getFieldErrors());
            ctx.render("books/publishers/form", model);
            return;
        }

        flashSuccess(ctx, "Publisher updated successfully.");
        ctx.redirect("/books/publishers");
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