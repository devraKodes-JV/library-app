package com.library.books.infrastructure.web.controller.author;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.author.AuthorDetailResponseDTO;
import com.library.books.application.service.author.GetAuthorDetailUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ShowAuthorController extends BaseController {

    private final GetAuthorDetailUseCase getAuthorDetailUseCase;

    public ShowAuthorController(GetAuthorDetailUseCase getAuthorDetailUseCase, WebControllerContext webContext) {
        super(webContext);
        this.getAuthorDetailUseCase = getAuthorDetailUseCase;
    }

    public void showAuthor(Context ctx) {
        requireCan(ctx, "authors.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        AuthorDetailResponseDTO author;
        try {
            author = getAuthorDetailUseCase.execute(id);
        } catch (com.library.books.domain.exception.AuthorNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("Author not found");
        }
        ctx.render("books/authors/show", buildShowModel(ctx, Map.of(
                "author", author,
                "relatedWorks", author.relatedWorks())));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", hasPermission(ctx, "authors.update"));
        model.put("canDelete", hasPermission(ctx, "authors.delete"));
        model.putAll(extra);
        return model;
    }

}