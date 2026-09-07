package com.library.stock.infrastructure.web.controller.stockitem;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.command.stockitem.ChangeStockItemStateCommand;
import com.library.stock.application.service.stockitem.ChangeStockItemStateUseCase;

import io.javalin.http.Context;

public class ChangeStockItemStateController extends BaseController {

    private final ChangeStockItemStateUseCase changeStockItemStateUseCase;

    public ChangeStockItemStateController(ChangeStockItemStateUseCase changeStockItemStateUseCase,
                                          WebControllerContext webContext) {
        super(webContext);
        this.changeStockItemStateUseCase = changeStockItemStateUseCase;
    }

    public void changeState(Context ctx) {
        requireCan(ctx, "stock.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        String state = ctx.formParam("state");
        try {
            changeStockItemStateUseCase.execute(new ChangeStockItemStateCommand(id, state));
            flashSuccess(ctx, "Stock item state updated.");
        } catch (Exception e) {
            flashDanger(ctx, e.getMessage());
        }
        ctx.redirect("/stock/items/" + id);
    }
}
