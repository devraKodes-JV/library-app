package com.library.stock.infrastructure.web.controller.stockitem;

import java.util.Map;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.service.stockmovement.MoveStockItemUseCase;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.port.out.StockLocationRepository;

import io.javalin.http.Context;

import java.util.List;
import java.util.LinkedHashMap;

public class MoveStockItemController extends BaseController {

    private final MoveStockItemUseCase moveStockItemUseCase;
    private final StockItemRepository stockItemRepository;
    private final StockLocationRepository stockLocationRepository;

    public MoveStockItemController(MoveStockItemUseCase moveStockItemUseCase,
                                    StockItemRepository stockItemRepository,
                                    StockLocationRepository stockLocationRepository,
                                    WebControllerContext webContext) {
        super(webContext);
        this.moveStockItemUseCase = moveStockItemUseCase;
        this.stockItemRepository = stockItemRepository;
        this.stockLocationRepository = stockLocationRepository;
    }

    public void move(Context ctx) {
        requireCan(ctx, "stock.update");
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        Long targetLocationId = ctx.formParamAsClass("targetLocationId", Long.class).get();
        String reason = ctx.formParam("reason");
        moveStockItemUseCase.execute(id, targetLocationId, reason, currentUserName(ctx));
        flashSuccess(ctx, "Stock item moved successfully.");
        ctx.redirect("/stock/items");
    }
}
