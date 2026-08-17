package com.library.books.infrastructure.web.controller.work;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.work.UpdateWorkCommand;
import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.service.work.UpdateWorkUseCase;
import com.library.books.application.service.work.GetWorkUseCase;
import com.library.books.application.service.work.ListWorksUseCase;
import com.library.books.application.service.author.ListAuthorsUseCase;
import com.library.books.application.service.category.ListCategoriesUseCase;
import com.library.books.application.service.language.ListLanguagesUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class UpdateWorkController {

    private final UpdateWorkUseCase updateWorkUseCase;
    private final GetWorkUseCase getWorkUseCase;
    private final ListWorksUseCase listWorksUseCase;
    private final ListLanguagesUseCase listLanguagesUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final ListAuthorsUseCase listAuthorsUseCase;
    private final WebControllerContext webContext;

    public UpdateWorkController(UpdateWorkUseCase updateWorkUseCase, GetWorkUseCase getWorkUseCase, ListWorksUseCase listWorksUseCase, ListLanguagesUseCase listLanguagesUseCase, ListCategoriesUseCase listCategoriesUseCase, ListAuthorsUseCase listAuthorsUseCase, WebControllerContext webContext) {
        this.updateWorkUseCase = updateWorkUseCase;
        this.getWorkUseCase = getWorkUseCase;
        this.listWorksUseCase = listWorksUseCase;
        this.listLanguagesUseCase = listLanguagesUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
        this.listAuthorsUseCase = listAuthorsUseCase;
        this.webContext = webContext;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "works.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        WorkResponseDTO work = getWorkUseCase.execute(id);
        List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
        List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
        List<AuthorResponseDTO> authors = listAuthorsUseCase.execute();
        ctx.render("books/works/form", buildEditModel(ctx, Map.of(
                "work", work,
                "languages", languages,
                "categories", categories,
                "authors", authors)));
    }

    public void updateWork(Context ctx) {
        requireCan(ctx, "works.update");
        UpdateWorkCommand command = new UpdateWorkCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                ctx.formParam("title"),
                ctx.formParam("subtitle"),
                parseLong(ctx.formParam("originalLanguageId")),
                parseLong(ctx.formParam("categoryId")),
                ctx.formParam("summary"),
                ctx.formParams("authorIds"));

        try {
            updateWorkUseCase.execute(command);
            WebHelper.flashSuccess(ctx, "Work updated successfully.");
            ctx.redirect("/books/works");
        } catch (ValidationException e) {
            WorkResponseDTO work = getWorkUseCase.execute(command.id());
            List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
            List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
            List<AuthorResponseDTO> authors = listAuthorsUseCase.execute();
            Map<String, Object> model = buildEditModel(ctx, Map.of(
                    "work", work,
                    "languages", languages,
                    "categories", categories,
                    "authors", authors));
            model.put("validationError", true);
            model.putAll(e.getFieldErrors());
            ctx.render("books/works/form", model);
            return;
        }
    }

    private Map<String, Object> buildEditModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "edit");
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
