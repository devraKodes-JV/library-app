package com.library.books.infrastructure.web.controller.bookFormat;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.bookFormat.ListBookFormatsUseCase;
import com.library.books.application.service.edition.ListEditionsByFormatUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ListBookFormatsController extends BaseController {

    private final ListBookFormatsUseCase listBookFormatsUseCase;

    public ListBookFormatsController(ListBookFormatsUseCase listBookFormatsUseCase, WebControllerContext webContext) {
        super(webContext);
        this.listBookFormatsUseCase = listBookFormatsUseCase;
    }

    public void listFormats(Context ctx) {
        requireCan(ctx, "formats.read");
        List<BookFormatResponseDTO> formats = listBookFormatsUseCase.execute();
        ctx.render("books/formats/list", buildListModel(ctx, Map.of(
                "formats", formats)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "formats.create"));
        model.put("canUpdate", hasPermission(ctx, "formats.update"));
        model.put("canDelete", hasPermission(ctx, "formats.delete"));
        model.putAll(extra);
        return model;
    }

}