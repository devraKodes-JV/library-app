package com.library.books.infrastructure.web.controller.bookFormat;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.command.bookFormat.UpdateBookFormatCommand;
import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.service.bookFormat.UpdateBookFormatUseCase;
import com.library.books.application.service.bookFormat.GetBookFormatUseCase;
import com.library.books.domain.exception.ValidationException;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class UpdateBookFormatController extends BaseController {

    private final UpdateBookFormatUseCase updateBookFormatUseCase;
    private final GetBookFormatUseCase getBookFormatUseCase;

    public UpdateBookFormatController(UpdateBookFormatUseCase updateBookFormatUseCase, GetBookFormatUseCase getBookFormatUseCase, WebControllerContext webContext) {
        super(webContext);
        this.updateBookFormatUseCase = updateBookFormatUseCase;
        this.getBookFormatUseCase = getBookFormatUseCase;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "formats.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        BookFormatResponseDTO format = getBookFormatUseCase.execute(id);
        ctx.render("books/formats/form", buildEditModel(ctx, Map.of("format", format)));
    }

    public void updateFormat(Context ctx) {
        requireCan(ctx, "formats.update");
        UpdateBookFormatCommand command = new UpdateBookFormatCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                ctx.formParam("code"),
                ctx.formParam("name"),
                ctx.formParam("description"));

        try {
            updateBookFormatUseCase.execute(command);
        } catch (ValidationException e) {
            BookFormatResponseDTO format = getBookFormatUseCase.execute(command.id());
            Map<String, Object> model = buildEditModel(ctx, Map.of("format", format));
            model.putAll(e.getFieldErrors());
            ctx.render("books/formats/form", model);
            return;
        }

        flashSuccess(ctx, "Format updated successfully.");
        ctx.redirect("/books/formats");
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

}