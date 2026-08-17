package com.library.books.infrastructure.web.controller.work;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.work.CreateWorkCommand;
import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.service.work.CreateWorkUseCase;
import com.library.books.application.service.work.ListWorksUseCase;
import com.library.books.application.service.author.ListAuthorsUseCase;
import com.library.books.application.service.category.ListCategoriesUseCase;
import com.library.books.application.service.language.ListLanguagesUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class CreateWorkController {

    private final CreateWorkUseCase createWorkUseCase;
    private final ListWorksUseCase listWorksUseCase;
    private final ListLanguagesUseCase listLanguagesUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final ListAuthorsUseCase listAuthorsUseCase;
    private final WebControllerContext webContext;

    public CreateWorkController(CreateWorkUseCase createWorkUseCase, ListWorksUseCase listWorksUseCase, ListLanguagesUseCase listLanguagesUseCase, ListCategoriesUseCase listCategoriesUseCase, ListAuthorsUseCase listAuthorsUseCase, WebControllerContext webContext) {
        this.createWorkUseCase = createWorkUseCase;
        this.listWorksUseCase = listWorksUseCase;
        this.listLanguagesUseCase = listLanguagesUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
        this.listAuthorsUseCase = listAuthorsUseCase;
        this.webContext = webContext;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "works.create");
        List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
        List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
        List<AuthorResponseDTO> authors = listAuthorsUseCase.execute();
        ctx.render("books/works/form", buildCreateModel(ctx, Map.of(
                "languages", languages,
                "categories", categories,
                "authors", authors)));
    }

    public void createWork(Context ctx) {
        requireCan(ctx, "works.create");
        CreateWorkCommand command = new CreateWorkCommand(
                ctx.formParam("title"),
                ctx.formParam("subtitle"),
                parseLong(ctx.formParam("originalLanguageId")),
                parseLong(ctx.formParam("categoryId")),
                ctx.formParam("summary"),
                ctx.formParams("authorIds"));

        try {
            createWorkUseCase.execute(command);
            WebHelper.flashSuccess(ctx, "Work created successfully.");
            ctx.redirect("/books/works");
        } catch (ValidationException e) {
            List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
            List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
            List<AuthorResponseDTO> authors = listAuthorsUseCase.execute();
            Map<String, Object> model = buildCreateModel(ctx, Map.of(
                    "languages", languages,
                    "categories", categories,
                    "authors", authors));
            model.put("validationError", true);
            model.putAll(e.getFieldErrors());
            ctx.render("books/works/form", model);
            return;
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("work", null);
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
}
