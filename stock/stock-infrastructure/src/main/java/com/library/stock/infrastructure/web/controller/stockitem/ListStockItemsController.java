package com.library.stock.infrastructure.web.controller.stockitem;

import java.util.List;
import java.util.Map;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.StockItemDTO;
import com.library.stock.application.service.stockitem.ListStockItemsUseCase;
import com.library.stock.infrastructure.web.EditionNamesProvider;

import io.javalin.http.Context;

public class ListStockItemsController extends BaseController {

    private final ListStockItemsUseCase listStockItemsUseCase;
    private final EditionNamesProvider editionNamesProvider;

    public ListStockItemsController(ListStockItemsUseCase listStockItemsUseCase,
                                    EditionNamesProvider editionNamesProvider,
                                    WebControllerContext webContext) {
        super(webContext);
        this.listStockItemsUseCase = listStockItemsUseCase;
        this.editionNamesProvider = editionNamesProvider;
    }

    public void listStockItems(Context ctx) {
        requireCan(ctx, "stock.read");
        String status = ctx.queryParamAsClass("status", String.class).getOrDefault("active");
        Map<Long, String> editionNames = editionNamesProvider.getEditionNames("all");
        List<StockItemDTO> items = listStockItemsUseCase.execute(status, editionNames);
        ctx.render("stock/items/list", buildListModel(ctx, Map.of(
                "items", items,
                "status", status)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> sections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", sections);
        model.put("canCreate", hasPermission(ctx, "stock.create"));
        model.put("canUpdate", hasPermission(ctx, "stock.update"));
        model.put("canDelete", hasPermission(ctx, "stock.delete"));
        model.put("canReactivate", hasPermission(ctx, "stock.reactivate"));
        model.putAll(extra);
        return model;
    }
}
