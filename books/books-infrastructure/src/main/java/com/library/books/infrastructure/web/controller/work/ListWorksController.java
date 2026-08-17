package com.library.books.infrastructure.web.controller.work;

import java.util.List;
import java.util.Map;

import com.library.kernel.web.Page;
import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.application.service.work.ListWorksUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListWorksController {

    private final ListWorksUseCase listWorksUseCase;
    private final WebControllerContext webContext;

    public ListWorksController(ListWorksUseCase listWorksUseCase, WebControllerContext webContext) {
        this.listWorksUseCase = listWorksUseCase;
        this.webContext = webContext;
    }

    public void listWorks(Context ctx) {
        requireCan(ctx, "works.read");
        int page = ctx.queryParamAsClass("page", Integer.class).getOrDefault(0);
        int size = ctx.queryParamAsClass("size", Integer.class).getOrDefault(20);
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 20;
        Page<WorkResponseDTO> workPage = listWorksUseCase.execute(page, size);
        ctx.render("books/works/list", buildListModel(ctx, Map.of(
                "works", workPage.items(),
                "workPage", workPage)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = webContext.currentUser(ctx);
        List<?> navSections = webContext.navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", webContext.hasPermission(ctx, "works.create"));
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
