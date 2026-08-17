package com.library.books.infrastructure.web.controller.author;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.service.author.ListAuthorsUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListAuthorsController {

    private final ListAuthorsUseCase listAuthorsUseCase;
    private final WebControllerContext webContext;

    public ListAuthorsController(ListAuthorsUseCase listAuthorsUseCase, WebControllerContext webContext) {
        this.listAuthorsUseCase = listAuthorsUseCase;
        this.webContext = webContext;
    }

    public void listAuthors(Context ctx) {
        requireCan(ctx, "authors.read");
        List<AuthorResponseDTO> authors = listAuthorsUseCase.execute();
        ctx.render("books/authors/list", buildListModel(ctx, Map.of(
                "authors", authors)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", webContext.hasPermission(ctx, "authors.create"));
        model.put("canUpdate", webContext.hasPermission(ctx, "authors.update"));
        model.put("canDelete", webContext.hasPermission(ctx, "authors.delete"));
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
