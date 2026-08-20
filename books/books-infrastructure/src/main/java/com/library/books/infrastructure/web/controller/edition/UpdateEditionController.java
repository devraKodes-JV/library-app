package com.library.books.infrastructure.web.controller.edition;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.edition.UpdateEditionCommand;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.dto.response.authorRole.AuthorRoleResponseDTO;
import com.library.books.application.service.edition.UpdateEditionUseCase;
import com.library.books.application.service.edition.GetEditionUseCase;
import com.library.books.application.service.work.ListWorksUseCase;
import com.library.books.application.service.publisher.ListPublishersUseCase;
import com.library.books.application.service.bookFormat.ListBookFormatsUseCase;
import com.library.books.application.service.language.ListLanguagesUseCase;
import com.library.books.application.service.author.ListAuthorsUseCase;
import com.library.books.application.service.authorRole.ListAuthorRolesUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class UpdateEditionController extends BaseController {

    private final UpdateEditionUseCase updateEditionUseCase;
    private final GetEditionUseCase getEditionUseCase;
    private final ListWorksUseCase listWorksUseCase;
    private final ListPublishersUseCase listPublishersUseCase;
    private final ListBookFormatsUseCase listBookFormatsUseCase;
    private final ListLanguagesUseCase listLanguagesUseCase;
    private final ListAuthorsUseCase listAuthorsUseCase;
    private final ListAuthorRolesUseCase listAuthorRolesUseCase;

    public UpdateEditionController(UpdateEditionUseCase updateEditionUseCase, GetEditionUseCase getEditionUseCase, ListWorksUseCase listWorksUseCase, ListPublishersUseCase listPublishersUseCase, ListBookFormatsUseCase listBookFormatsUseCase, ListLanguagesUseCase listLanguagesUseCase, ListAuthorsUseCase listAuthorsUseCase, ListAuthorRolesUseCase listAuthorRolesUseCase, WebControllerContext webContext) {
        super(webContext);
        this.updateEditionUseCase = updateEditionUseCase;
        this.getEditionUseCase = getEditionUseCase;
        this.listWorksUseCase = listWorksUseCase;
        this.listPublishersUseCase = listPublishersUseCase;
        this.listBookFormatsUseCase = listBookFormatsUseCase;
        this.listLanguagesUseCase = listLanguagesUseCase;
        this.listAuthorsUseCase = listAuthorsUseCase;
        this.listAuthorRolesUseCase = listAuthorRolesUseCase;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "editions.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        EditionResponseDTO edition = getEditionUseCase.execute(id);
        List<WorkResponseDTO> works = listWorksUseCase.execute(0, 100).items();
        List<PublisherResponseDTO> publishers = listPublishersUseCase.execute();
        List<BookFormatResponseDTO> formats = listBookFormatsUseCase.execute();
        List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
        List<AuthorResponseDTO> authors = listAuthorsUseCase.execute();
        List<AuthorRoleResponseDTO> authorRoles = listAuthorRolesUseCase.execute();
        ctx.render("books/editions/form", buildEditModel(ctx, Map.of(
                "edition", edition,
                "works", works,
                "publishers", publishers,
                "formats", formats,
                "languages", languages,
                "authorList", authors,
                "authorRoles", authorRoles)));
    }

    public void updateEdition(Context ctx) {
        requireCan(ctx, "editions.update");
        List<String> authorIds = ctx.formParams("authorIds");
        List<String> authorRoleIds = authorIds.stream()
                .map(id -> ctx.formParam("authorRoleId_" + id))
                .toList();
        UpdateEditionCommand command = new UpdateEditionCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                parseLong(ctx.formParam("workId")),
                parseLong(ctx.formParam("publisherId")),
                parseLong(ctx.formParam("formatId")),
                parseLong(ctx.formParam("languageId")),
                ctx.formParam("isbn"),
                parseInt(ctx.formParam("pages")),
                parseInt(ctx.formParam("publicationYear")),
                ctx.formParam("editionNumber"),
                authorIds,
                authorRoleIds);

        try {
            updateEditionUseCase.execute(command);
            flashSuccess(ctx, "Edition updated successfully.");
            ctx.redirect("/books/editions");
        } catch (ValidationException e) {
            EditionResponseDTO edition = getEditionUseCase.execute(command.id());
            List<WorkResponseDTO> works = listWorksUseCase.execute(0, 100).items();
            List<PublisherResponseDTO> publishers = listPublishersUseCase.execute();
            List<BookFormatResponseDTO> formats = listBookFormatsUseCase.execute();
            List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
            List<AuthorResponseDTO> authors = listAuthorsUseCase.execute();
            List<AuthorRoleResponseDTO> authorRoles = listAuthorRolesUseCase.execute();
            Map<String, Object> model = buildEditModel(ctx, Map.of(
                    "edition", edition,
                    "works", works,
                    "publishers", publishers,
                    "formats", formats,
                    "languages", languages,
                    "authorList", authors,
                    "authorRoles", authorRoles));
            model.putAll(e.getFieldErrors());
            ctx.render("books/editions/form", model);
            return;
        }
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


    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}