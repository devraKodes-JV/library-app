package com.library.books.infrastructure.web.controller.work;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.work.UpdateWorkCommand;
import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.dto.response.authorRole.AuthorRoleResponseDTO;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.service.work.UpdateWorkUseCase;
import com.library.books.application.service.work.GetWorkUseCase;
import com.library.books.application.service.work.ListWorksUseCase;
import com.library.books.application.service.author.ListAuthorsUseCase;
import com.library.books.application.service.authorRole.ListAuthorRolesUseCase;
import com.library.books.application.service.category.ListCategoriesUseCase;
import com.library.books.application.service.language.ListLanguagesUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class UpdateWorkController extends BaseController {

    private final UpdateWorkUseCase updateWorkUseCase;
    private final GetWorkUseCase getWorkUseCase;
    private final ListWorksUseCase listWorksUseCase;
    private final ListLanguagesUseCase listLanguagesUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final ListAuthorsUseCase listAuthorsUseCase;
    private final ListAuthorRolesUseCase listAuthorRolesUseCase;

    public UpdateWorkController(UpdateWorkUseCase updateWorkUseCase, GetWorkUseCase getWorkUseCase, ListWorksUseCase listWorksUseCase, ListLanguagesUseCase listLanguagesUseCase, ListCategoriesUseCase listCategoriesUseCase, ListAuthorsUseCase listAuthorsUseCase, ListAuthorRolesUseCase listAuthorRolesUseCase, WebControllerContext webContext) {
        super(webContext);
        this.updateWorkUseCase = updateWorkUseCase;
        this.getWorkUseCase = getWorkUseCase;
        this.listWorksUseCase = listWorksUseCase;
        this.listLanguagesUseCase = listLanguagesUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
        this.listAuthorsUseCase = listAuthorsUseCase;
        this.listAuthorRolesUseCase = listAuthorRolesUseCase;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "works.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        WorkResponseDTO work = getWorkUseCase.execute(id);
        List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
        List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
        List<AuthorResponseDTO> authors = listAuthorsUseCase.execute();
        List<AuthorRoleResponseDTO> authorRoles = listAuthorRolesUseCase.execute();
        ctx.render("books/works/form", buildEditModel(ctx, Map.of(
                "work", work,
                "languages", languages,
                "categories", categories,
                "authorList", authors,
                "authorRoles", authorRoles)));
    }

    public void updateWork(Context ctx) {
        requireCan(ctx, "works.update");
        List<String> authorIds = ctx.formParams("authorIds");
        List<String> authorRoleIds = authorIds.stream()
                .map(id -> ctx.formParam("authorRoleId_" + id))
                .toList();
        UpdateWorkCommand command = new UpdateWorkCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                ctx.formParam("title"),
                ctx.formParam("subtitle"),
                parseLong(ctx.formParam("originalLanguageId")),
                parseLong(ctx.formParam("categoryId")),
                ctx.formParam("summary"),
                authorIds,
                authorRoleIds);

        try {
            updateWorkUseCase.execute(command);
            flashSuccess(ctx, "Work updated successfully.");
            ctx.redirect("/books/works");
        } catch (ValidationException e) {
            WorkResponseDTO work = getWorkUseCase.execute(command.id());
            List<LanguageResponseDTO> languages = listLanguagesUseCase.execute();
            List<CategoryResponseDTO> categories = listCategoriesUseCase.execute();
            List<AuthorResponseDTO> authors = listAuthorsUseCase.execute();
            List<AuthorRoleResponseDTO> authorRoles = listAuthorRolesUseCase.execute();
            Map<String, Object> model = buildEditModel(ctx, Map.of(
                    "work", work,
                    "languages", languages,
                    "categories", categories,
                    "authorList", authors,
                    "authorRoles", authorRoles));
            model.put("validationError", true);
            model.putAll(e.getFieldErrors());
            ctx.render("books/works/form", model);
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
}