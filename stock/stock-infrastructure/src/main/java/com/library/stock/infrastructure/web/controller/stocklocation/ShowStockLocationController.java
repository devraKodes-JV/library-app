package com.library.stock.infrastructure.web.controller.stocklocation;

import java.util.List;
import java.util.Map;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.StockLocationDTO;
import com.library.stock.application.service.stocklocation.GetStockLocationUseCase;
import com.library.stock.domain.exception.StockLocationNotFoundException;

import io.javalin.http.Context;

public class ShowStockLocationController extends BaseController {

    private final GetStockLocationUseCase getStockLocationUseCase;

    public ShowStockLocationController(GetStockLocationUseCase getStockLocationUseCase,
                                       WebControllerContext webContext) {
        super(webContext);
        this.getStockLocationUseCase = getStockLocationUseCase;
    }

    public void showStockLocation(Context ctx) {
        requireCan(ctx, "stocklocations.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        StockLocationDTO location;
        try {
            location = getStockLocationUseCase.execute(id);
        } catch (StockLocationNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("StockLocation not found");
        }
        ctx.render("stock/locations/show", buildShowModel(ctx, Map.of("location", location)));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> sections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", sections);
        model.put("canUpdate", hasPermission(ctx, "stocklocations.update"));
        model.put("canDelete", hasPermission(ctx, "stocklocations.delete"));
        model.putAll(extra);
        return model;
    }
}
