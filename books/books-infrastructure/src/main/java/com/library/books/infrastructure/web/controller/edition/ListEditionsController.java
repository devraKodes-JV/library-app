package com.library.books.infrastructure.web.controller.edition;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.edition.ListEditionsUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ListEditionsController extends BaseController {

    private final ListEditionsUseCase listEditionsUseCase;

    public ListEditionsController(ListEditionsUseCase listEditionsUseCase, WebControllerContext webContext) {
        super(webContext);
        this.listEditionsUseCase = listEditionsUseCase;
    }

    public void listEditions(Context ctx) {
        requireCan(ctx, "editions.read");
        String status = ctx.queryParamAsClass("status", String.class).getOrDefault("active");
        List<EditionResponseDTO> editions = listEditionsUseCase.execute(status);
        ctx.render("books/editions/list", buildListModel(ctx, Map.of(
                "editions", editions,
                "status", status)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canCreate", hasPermission(ctx, "editions.create"));
        model.put("canUpdate", hasPermission(ctx, "editions.update"));
        model.put("canDelete", hasPermission(ctx, "editions.delete"));
        model.put("canReactivate", hasPermission(ctx, "editions.reactivate"));
        model.putAll(extra);
        return model;
    }

}