package com.library.books.infrastructure.web.controller.publisher;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.service.publisher.ListPublishersUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListPublishersController {

    private final ListPublishersUseCase listPublishersUseCase;
    private final WebControllerContext webContext;

    public ListPublishersController(ListPublishersUseCase listPublishersUseCase, WebControllerContext webContext) {
        this.listPublishersUseCase = listPublishersUseCase;
        this.webContext = webContext;
    }

    public void listPublishers(Context ctx) {
        requireCan(ctx, "publishers.read");
        List<PublisherResponseDTO> publishers = listPublishersUseCase.execute();
        ctx.render("books/publishers/list", buildListModel(ctx, Map.of(
                "publishers", publishers)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", webContext.hasPermission(ctx, "publishers.create"));
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
