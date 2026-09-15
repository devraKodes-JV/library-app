package com.library.stock.infrastructure.web.controller.stockmovement;

import java.util.Map;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.StockMovementDTO;
import com.library.stock.application.service.stockmovement.ListStockMovementsUseCase;

import io.javalin.http.Context;

import java.util.List;
import java.util.LinkedHashMap;

public class ListStockMovementsController extends BaseController {

    private final ListStockMovementsUseCase listStockMovementsUseCase;

    public ListStockMovementsController(ListStockMovementsUseCase listStockMovementsUseCase,
                                         WebControllerContext webContext) {
        super(webContext);
        this.listStockMovementsUseCase = listStockMovementsUseCase;
    }

    public void listMovements(Context ctx) {
        requireCan(ctx, "stock.read");
        List<StockMovementDTO> movements = listStockMovementsUseCase.execute();
        ctx.render("stock/movements/list", buildListModel(ctx, Map.of(
                "movements", movements)));
    }

    private Map<String, Object> buildListModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> sections = navSections(ctx);
        Map<String, Object> model = new LinkedHashMap<>();
        model.put("user", current);
        model.put("navSections", sections);
        model.putAll(extra);
        return model;
    }
}
