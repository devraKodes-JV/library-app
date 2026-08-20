package com.library.books.infrastructure.web.controller.author;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.service.author.ListAuthorsUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ListAuthorsController extends BaseController {

    private final ListAuthorsUseCase listAuthorsUseCase;

    public ListAuthorsController(ListAuthorsUseCase listAuthorsUseCase, WebControllerContext webContext) {
        super(webContext);
        this.listAuthorsUseCase = listAuthorsUseCase;
    }

    public void listAuthors(Context ctx) {
        requireCan(ctx, "authors.read");
        List<AuthorResponseDTO> authors = listAuthorsUseCase.execute();
        ctx.render("books/authors/list", buildListModel(ctx, Map.of(
                "authors", authors)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "authors.create"));
        model.put("canUpdate", hasPermission(ctx, "authors.update"));
        model.put("canDelete", hasPermission(ctx, "authors.delete"));
        model.putAll(extra);
        return model;
    }

}