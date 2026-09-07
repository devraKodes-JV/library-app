package com.library.stock.infrastructure.web.controller.stocklocation;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.command.stocklocation.ReactivateStockLocationCommand;
import com.library.stock.application.service.stocklocation.ReactivateStockLocationUseCase;
import com.library.stock.domain.exception.StockLocationNotFoundException;

import io.javalin.http.Context;

public class ReactivateStockLocationController extends BaseController {

    private final ReactivateStockLocationUseCase reactivateStockLocationUseCase;

    public ReactivateStockLocationController(ReactivateStockLocationUseCase reactivateStockLocationUseCase,
                                             WebControllerContext webContext) {
        super(webContext);
        this.reactivateStockLocationUseCase = reactivateStockLocationUseCase;
    }

    public void reactivateStockLocation(Context ctx) {
        requireCan(ctx, "stocklocations.reactivate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            reactivateStockLocationUseCase.execute(new ReactivateStockLocationCommand(id));
            flashSuccess(ctx, "Stock location reactivated.");
        } catch (StockLocationNotFoundException e) {
            flashDanger(ctx, "Stock location not found.");
        }
        ctx.redirect("/stock/locations");
    }
}
