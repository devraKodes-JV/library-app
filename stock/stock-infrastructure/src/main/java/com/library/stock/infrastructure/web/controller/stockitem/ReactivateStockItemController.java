package com.library.stock.infrastructure.web.controller.stockitem;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.command.stockitem.ReactivateStockItemCommand;
import com.library.stock.application.service.stockitem.ReactivateStockItemUseCase;
import com.library.stock.domain.exception.StockItemNotFoundException;

import io.javalin.http.Context;

public class ReactivateStockItemController extends BaseController {

    private final ReactivateStockItemUseCase reactivateStockItemUseCase;

    public ReactivateStockItemController(ReactivateStockItemUseCase reactivateStockItemUseCase,
                                         WebControllerContext webContext) {
        super(webContext);
        this.reactivateStockItemUseCase = reactivateStockItemUseCase;
    }

    public void reactivateStockItem(Context ctx) {
        requireCan(ctx, "stock.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivateStockItemUseCase.execute(new ReactivateStockItemCommand(id));
            flashSuccess(ctx, "Stock item reactivated.");
        } catch (StockItemNotFoundException e) {
            flashDanger(ctx, "Stock item not found.");
        }
        ctx.redirect("/stock/items");
    }
}
