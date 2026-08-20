package com.library.books.infrastructure.web.controller.publisher;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.publisher.GetPublisherUseCase;
import com.library.books.application.service.edition.ListEditionsByPublisherUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ShowPublisherController extends BaseController {

    private final GetPublisherUseCase getPublisherUseCase;
    private final ListEditionsByPublisherUseCase listEditionsByPublisherUseCase;

    public ShowPublisherController(GetPublisherUseCase getPublisherUseCase, ListEditionsByPublisherUseCase listEditionsByPublisherUseCase, WebControllerContext webContext) {
        super(webContext);
        this.getPublisherUseCase = getPublisherUseCase;
        this.listEditionsByPublisherUseCase = listEditionsByPublisherUseCase;
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
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", hasPermission(ctx, "publishers.update"));
        model.put("canDelete", hasPermission(ctx, "publishers.delete"));
        model.putAll(extra);
        return model;
    }

}