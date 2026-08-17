package com.library.books.infrastructure.web.controller.publisher;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.publisher.GetPublisherUseCase;
import com.library.books.application.service.edition.ListEditionsByPublisherUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ShowPublisherController {

    private final GetPublisherUseCase getPublisherUseCase;
    private final ListEditionsByPublisherUseCase listEditionsByPublisherUseCase;
    private final WebControllerContext webContext;

    public ShowPublisherController(GetPublisherUseCase getPublisherUseCase, ListEditionsByPublisherUseCase listEditionsByPublisherUseCase, WebControllerContext webContext) {
        this.getPublisherUseCase = getPublisherUseCase;
        this.listEditionsByPublisherUseCase = listEditionsByPublisherUseCase;
        this.webContext = webContext;
    }

    public void showPublisher(Context ctx) {
        requireCan(ctx, "publishers.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        PublisherResponseDTO publisher;
        try {
            publisher = getPublisherUseCase.execute(id);
        } catch (com.library.books.domain.exception.PublisherNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("Publisher not found");
        }
        var relatedEditions = listEditionsByPublisherUseCase.execute(id);
        ctx.render("books/publishers/show", buildShowModel(ctx, Map.of(
                "publisher", publisher,
                "relatedEditions", relatedEditions)));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", webContext.hasPermission(ctx, "publishers.update"));
        model.put("canDelete", webContext.hasPermission(ctx, "publishers.delete"));
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
