package com.library.stock.infrastructure.web.controller.stocklocation;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.command.stocklocation.DeleteStockLocationCommand;
import com.library.stock.application.service.stocklocation.DeleteStockLocationUseCase;

import io.javalin.http.Context;

public class DeleteStockLocationController extends BaseController {

    private final DeleteStockLocationUseCase deleteStockLocationUseCase;

    public DeleteStockLocationController(DeleteStockLocationUseCase deleteStockLocationUseCase,
                                         WebControllerContext webContext) {
        super(webContext);
        this.deleteStockLocationUseCase = deleteStockLocationUseCase;
    }

    public void deleteStockLocation(Context ctx) {
        requireCan(ctx, "stocklocations.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        try {
            deleteStockLocationUseCase.execute(new DeleteStockLocationCommand(id));
            flashWarning(ctx, "Stock location deleted.");
        } catch (IllegalStateException e) {
            flashDanger(ctx, e.getMessage());
        }
        ctx.redirect("/stock/locations");
    }
}
