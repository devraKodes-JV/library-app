package com.library.books.infrastructure.web.controller.edition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.edition.GetEditionUseCase;
import com.library.books.domain.model.Author;
import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.port.out.AuthorRepository;
import com.library.books.domain.port.out.AuthorRoleRepository;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ShowEditionController extends BaseController {

    private final GetEditionUseCase getEditionUseCase;
    private final com.library.books.application.service.author.ListAuthorsUseCase listAuthorsUseCase;
    private final com.library.books.application.service.authorRole.ListAuthorRolesUseCase listAuthorRolesUseCase;

    public ShowEditionController(GetEditionUseCase getEditionUseCase, com.library.books.application.service.author.ListAuthorsUseCase listAuthorsUseCase, com.library.books.application.service.authorRole.ListAuthorRolesUseCase listAuthorRolesUseCase, WebControllerContext webContext) {
        super(webContext);
        this.getEditionUseCase = getEditionUseCase;
        this.listAuthorsUseCase = listAuthorsUseCase;
        this.listAuthorRolesUseCase = listAuthorRolesUseCase;
    }

    public void showEdition(Context ctx) {
        requireCan(ctx, "editions.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        EditionResponseDTO edition;
        try {
            edition = getEditionUseCase.execute(id);
        } catch (com.library.books.domain.exception.EditionNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("Edition not found");
        }
        
        var authors = listAuthorsUseCase.execute();
        var authorNames = authors.stream().collect(Collectors.toMap(a -> a.id(), a -> a.fullName(), (a, b) -> a));
        var authorRoles = listAuthorRolesUseCase.execute();
        var authorRoleNames = authorRoles.stream().collect(Collectors.toMap(r -> r.id(), r -> r.name(), (a, b) -> a));
        
        ctx.render("books/editions/show", buildShowModel(ctx, Map.of(
                "edition", edition,
                "authorNames", authorNames,
                "authorRoleNames", authorRoleNames)));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", hasPermission(ctx, "editions.update"));
        model.put("canDelete", hasPermission(ctx, "editions.delete"));
        model.putAll(extra);
        return model;
    }

}