package com.library.books.infrastructure.web.controller.bookFormat;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.bookFormat.GetBookFormatUseCase;
import com.library.books.application.service.edition.ListEditionsByFormatUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ShowBookFormatController extends BaseController {

    private final GetBookFormatUseCase getBookFormatUseCase;
    private final ListEditionsByFormatUseCase listEditionsByFormatUseCase;

    public ShowBookFormatController(GetBookFormatUseCase getBookFormatUseCase, ListEditionsByFormatUseCase listEditionsByFormatUseCase, WebControllerContext webContext) {
        super(webContext);
        this.getBookFormatUseCase = getBookFormatUseCase;
        this.listEditionsByFormatUseCase = listEditionsByFormatUseCase;
    }

    public void showFormat(Context ctx) {
        requireCan(ctx, "formats.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        BookFormatResponseDTO format;
        try {
            format = getBookFormatUseCase.execute(id);
        } catch (com.library.books.domain.exception.BookFormatNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("Format not found");
        }
        var relatedEditions = listEditionsByFormatUseCase.execute(id);
        ctx.render("books/formats/show", buildShowModel(ctx, Map.of(
                "format", format,
                "relatedEditions", relatedEditions)));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", hasPermission(ctx, "formats.update"));
        model.put("canDelete", hasPermission(ctx, "formats.delete"));
        model.putAll(extra);
        return model;
    }

}