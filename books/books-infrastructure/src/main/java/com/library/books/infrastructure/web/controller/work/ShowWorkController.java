package com.library.books.infrastructure.web.controller.work;

import java.util.List;
import java.util.Map;

import com.library.books.application.dto.response.work.WorkDetailResponseDTO;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.service.work.GetWorkDetailUseCase;
import com.library.books.application.service.edition.ListEditionsByWorkUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class ShowWorkController extends BaseController {

    private final GetWorkDetailUseCase getWorkDetailUseCase;
    private final ListEditionsByWorkUseCase listEditionsByWorkUseCase;

    public ShowWorkController(GetWorkDetailUseCase getWorkDetailUseCase, ListEditionsByWorkUseCase listEditionsByWorkUseCase, WebControllerContext webContext) {
        super(webContext);
        this.getWorkDetailUseCase = getWorkDetailUseCase;
        this.listEditionsByWorkUseCase = listEditionsByWorkUseCase;
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
        var current = currentUser(ctx);
        List<?> navSections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", navSections);
        model.put("canUpdate", hasPermission(ctx, "works.update"));
        model.put("canDelete", hasPermission(ctx, "works.delete"));
        model.putAll(extra);
        return model;
    }

}