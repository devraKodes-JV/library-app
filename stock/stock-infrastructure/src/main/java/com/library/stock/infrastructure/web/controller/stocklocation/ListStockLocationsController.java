package com.library.stock.infrastructure.web.controller.stocklocation;

import java.util.List;
import java.util.Map;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.StockLocationDTO;
import com.library.stock.application.service.stocklocation.ListStockLocationsUseCase;

import io.javalin.http.Context;

public class ListStockLocationsController extends BaseController {

    private final ListStockLocationsUseCase listStockLocationsUseCase;

    public ListStockLocationsController(ListStockLocationsUseCase listStockLocationsUseCase,
                                        WebControllerContext webContext) {
        super(webContext);
        this.listStockLocationsUseCase = listStockLocationsUseCase;
    }

    public void listStockLocations(Context ctx) {
        requireCan(ctx, "stocklocations.read");
        String status = ctx.queryParamAsClass("status", String.class).getOrDefault("active");
        List<StockLocationDTO> locations = listStockLocationsUseCase.execute(status);
        ctx.render("stock/locations/list", buildListModel(ctx, Map.of(
                "locations", locations,
                "status", status)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> sections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", sections);
        model.put("canCreate", hasPermission(ctx, "stocklocations.create"));
        model.put("canUpdate", hasPermission(ctx, "stocklocations.update"));
        model.put("canDelete", hasPermission(ctx, "stocklocations.delete"));
        model.put("canReactivate", hasPermission(ctx, "stocklocations.reactivate"));
        model.putAll(extra);
        return model;
    }
}
