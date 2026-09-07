package com.library.stock.infrastructure.web.controller.stockitem;

import java.util.List;
import java.util.Map;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.StockItemDTO;
import com.library.stock.domain.exception.StockItemNotFoundException;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.port.out.StockLocationRepository;
import com.library.stock.infrastructure.web.EditionNamesProvider;

import io.javalin.http.Context;

public class ShowStockItemController extends BaseController {

    private final StockItemRepository stockItemRepository;
    private final EditionNamesProvider editionNamesProvider;
    private final StockLocationRepository stockLocationRepository;

    public ShowStockItemController(StockItemRepository stockItemRepository,
                                   EditionNamesProvider editionNamesProvider,
                                   StockLocationRepository stockLocationRepository,
                                   WebControllerContext webContext) {
        super(webContext);
        this.stockItemRepository = stockItemRepository;
        this.editionNamesProvider = editionNamesProvider;
        this.stockLocationRepository = stockLocationRepository;
    }

    public void showStockItem(Context ctx) {
        requireCan(ctx, "stock.read");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        var item = stockItemRepository.findById(id)
                .orElseThrow(() -> new StockItemNotFoundException(id));
        Map<Long, String> editionNames = editionNamesProvider.getEditionNames("all");
        String editionName = editionNames.getOrDefault(item.getEditionId(), "");
        Map<Long, String> locationNames = stockLocationRepository.findNamesByIds(List.of(item.getLocationId()));
        String locationName = locationNames.getOrDefault(item.getLocationId(), "");
        ctx.render("stock/items/show", buildShowModel(ctx, Map.of(
                "item", StockItemDTO.of(item, editionName, locationName))));
    }

    private Map<String, Object> buildShowModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> sections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", sections);
        model.put("canUpdate", hasPermission(ctx, "stock.update"));
        model.put("canDelete", hasPermission(ctx, "stock.delete"));
        model.putAll(extra);
        return model;
    }
}
