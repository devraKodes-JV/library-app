package com.library.stock.infrastructure.web.controller.stockitem;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.command.stockitem.DeleteStockItemCommand;
import com.library.stock.application.service.stockitem.DeleteStockItemUseCase;

import io.javalin.http.Context;

public class DeleteStockItemController extends BaseController {

    private final DeleteStockItemUseCase deleteStockItemUseCase;

    public DeleteStockItemController(DeleteStockItemUseCase deleteStockItemUseCase,
                                     WebControllerContext webContext) {
        super(webContext);
        this.deleteStockItemUseCase = deleteStockItemUseCase;
    }

    public void deleteStockItem(Context ctx) {
        requireCan(ctx, "stock.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteStockItemUseCase.execute(new DeleteStockItemCommand(id));
        flashWarning(ctx, "Stock item deleted.");
        ctx.redirect("/stock/items");
    }
}
