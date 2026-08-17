package com.library.books.infrastructure.web.controller.bookFormat;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.bookFormat.ListBookFormatsUseCase;
import com.library.books.application.service.edition.ListEditionsByFormatUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListBookFormatsController {

    private final ListBookFormatsUseCase listBookFormatsUseCase;
    private final WebControllerContext webContext;

    public ListBookFormatsController(ListBookFormatsUseCase listBookFormatsUseCase, WebControllerContext webContext) {
        this.listBookFormatsUseCase = listBookFormatsUseCase;
        this.webContext = webContext;
    }

    public void listFormats(Context ctx) {
        requireCan(ctx, "formats.read");
        List<BookFormatResponseDTO> formats = listBookFormatsUseCase.execute();
        ctx.render("books/formats/list", buildListModel(ctx, Map.of(
                "formats", formats)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", webContext.hasPermission(ctx, "formats.create"));
        model.put("canUpdate", webContext.hasPermission(ctx, "formats.update"));
        model.put("canDelete", webContext.hasPermission(ctx, "formats.delete"));
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
