package com.library.books.infrastructure.web.controller.edition;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.edition.GetEditionUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ShowEditionController {

    private final GetEditionUseCase getEditionUseCase;
    private final WebControllerContext webContext;

    public ShowEditionController(GetEditionUseCase getEditionUseCase, WebControllerContext webContext) {
        this.getEditionUseCase = getEditionUseCase;
        this.webContext = webContext;
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
        ctx.render("books/editions/show", buildShowModel(ctx, Map.of(
                "edition", edition)));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", webContext.hasPermission(ctx, "editions.update"));
        model.put("canDelete", webContext.hasPermission(ctx, "editions.delete"));
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
