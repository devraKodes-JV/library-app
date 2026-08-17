package com.library.books.infrastructure.web.controller.edition;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.edition.CreateEditionCommand;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.dto.response.publisher.PublisherResponseDTO;
import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.service.edition.CreateEditionUseCase;
import com.library.books.application.service.work.ListWorksUseCase;
import com.library.books.application.service.publisher.ListPublishersUseCase;
import com.library.books.application.service.bookFormat.ListBookFormatsUseCase;
import com.library.books.application.service.language.ListLanguagesUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class CreateEditionController {

    private final CreateEditionUseCase createEditionUseCase;
    private final ListWorksUseCase listWorksUseCase;
    private final ListPublishersUseCase listPublishersUseCase;
    private final ListBookFormatsUseCase listBookFormatsUseCase;
    private final ListLanguagesUseCase listLanguagesUseCase;
    private final WebControllerContext webContext;

    public CreateEditionController(CreateEditionUseCase createEditionUseCase, ListWorksUseCase listWorksUseCase, ListPublishersUseCase listPublishersUseCase, ListBookFormatsUseCase listBookFormatsUseCase, ListLanguagesUseCase listLanguagesUseCase, WebControllerContext webContext) {
        this.createEditionUseCase = createEditionUseCase;
        this.listWorksUseCase = listWorksUseCase;
        this.listPublishersUseCase = listPublishersUseCase;
        this.listBookFormatsUseCase = listBookFormatsUseCase;
        this.listLanguagesUseCase = listLanguagesUseCase;
        this.webContext = webContext;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "editions.create");
        List<WorkResponseDTO> works = listWorksUseCase.execute(0, 100).items();
        List<PublisherResponseDTO> publishers = listPublishersUseCase.execute();
        List<BookFormatResponseDTO> formats = listBookFormatsUseCase.execute();
        List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
        ctx.render("books/editions/form", buildCreateModel(ctx, Map.of(
                "works", works,
                "publishers", publishers,
                "formats", formats,
                "languages", languages)));
    }

    public void createEdition(Context ctx) {
        requireCan(ctx, "editions.create");
        CreateEditionCommand command = new CreateEditionCommand(
                parseLong(ctx.formParam("workId")),
                parseLong(ctx.formParam("publisherId")),
                parseLong(ctx.formParam("formatId")),
                parseLong(ctx.formParam("languageId")),
                ctx.formParam("isbn"),
                parseInt(ctx.formParam("pages")),
                parseInt(ctx.formParam("publicationYear")),
                ctx.formParam("editionNumber"));

        try {
            createEditionUseCase.execute(command);
            WebHelper.flashSuccess(ctx, "Edition created successfully.");
            ctx.redirect("/books/editions");
        } catch (ValidationException e) {
            List<WorkResponseDTO> works = listWorksUseCase.execute(0, 100).items();
            List<PublisherResponseDTO> publishers = listPublishersUseCase.execute();
            List<BookFormatResponseDTO> formats = listBookFormatsUseCase.execute();
            List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
            Map<String, Object> model = buildCreateModel(ctx, Map.of(
                    "works", works,
                    "publishers", publishers,
                    "formats", formats,
                    "languages", languages));
            model.putAll(e.getFieldErrors());
            ctx.render("books/editions/form", model);
            return;
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("edition", null);
        model.put("user", current);
        model.put("navSections", navSections);
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
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
