package com.library.books.infrastructure.web.controller.publisher;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.service.publisher.ListPublishersUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ListPublishersController extends BaseController {

    private final ListPublishersUseCase listPublishersUseCase;

    public ListPublishersController(ListPublishersUseCase listPublishersUseCase, WebControllerContext webContext) {
        super(webContext);
        this.listPublishersUseCase = listPublishersUseCase;
    }

    public void listPublishers(Context ctx) {
        requireCan(ctx, "publishers.read");
        List<PublisherResponseDTO> publishers = listPublishersUseCase.execute();
        ctx.render("books/publishers/list", buildListModel(ctx, Map.of(
                "publishers", publishers)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "publishers.create"));
        model.put("canUpdate", hasPermission(ctx, "publishers.update"));
        model.put("canDelete", hasPermission(ctx, "publishers.delete"));
        model.putAll(extra);
        return model;
    }

}