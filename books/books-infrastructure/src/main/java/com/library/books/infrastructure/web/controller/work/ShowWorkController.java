package com.library.books.infrastructure.web.controller.work;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.work.WorkDetailResponseDTO;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.work.GetWorkDetailUseCase;
import com.library.books.application.service.edition.ListEditionsByWorkUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ShowWorkController {

    private final GetWorkDetailUseCase getWorkDetailUseCase;
    private final ListEditionsByWorkUseCase listEditionsByWorkUseCase;
    private final WebControllerContext webContext;

    public ShowWorkController(GetWorkDetailUseCase getWorkDetailUseCase, ListEditionsByWorkUseCase listEditionsByWorkUseCase, WebControllerContext webContext) {
        this.getWorkDetailUseCase = getWorkDetailUseCase;
        this.listEditionsByWorkUseCase = listEditionsByWorkUseCase;
        this.webContext = webContext;
    }

    public void showWork(Context ctx) {
        requireCan(ctx, "works.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        WorkDetailResponseDTO work;
        try {
            work = getWorkDetailUseCase.execute(id);
        } catch (com.library.books.domain.exception.WorkNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("Work not found");
        }
        var relatedEditions = listEditionsByWorkUseCase.execute(id);
        ctx.render("books/works/show", buildShowModel(ctx, Map.of(
                "work", work,
                "relatedEditions", relatedEditions)));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", webContext.hasPermission(ctx, "works.update"));
        model.put("canDelete", webContext.hasPermission(ctx, "works.delete"));
        model.putAll(extra);
        return model;
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
