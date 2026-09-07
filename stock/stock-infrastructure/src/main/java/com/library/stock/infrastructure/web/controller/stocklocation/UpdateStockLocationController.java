package com.library.stock.infrastructure.web.controller.stocklocation;

import java.util.List;
import java.util.Map;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.StockLocationDTO;
import com.library.stock.application.dto.command.stocklocation.UpdateStockLocationCommand;
import com.library.stock.application.service.stocklocation.GetStockLocationUseCase;
import com.library.stock.application.service.stocklocation.UpdateStockLocationUseCase;
import com.library.stock.domain.exception.StockLocationNotFoundException;
import com.library.stock.domain.exception.ValidationException;

import io.javalin.http.Context;

public class UpdateStockLocationController extends BaseController {

    private final UpdateStockLocationUseCase updateStockLocationUseCase;
    private final GetStockLocationUseCase getStockLocationUseCase;

    public UpdateStockLocationController(UpdateStockLocationUseCase updateStockLocationUseCase,
                                         GetStockLocationUseCase getStockLocationUseCase,
                                         WebControllerContext webContext) {
        super(webContext);
        this.updateStockLocationUseCase = updateStockLocationUseCase;
        this.getStockLocationUseCase = getStockLocationUseCase;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "stocklocations.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        StockLocationDTO location;
        try {
            location = getStockLocationUseCase.execute(id);
        } catch (StockLocationNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("StockLocation not found");
        }
        ctx.render("stock/locations/form", buildEditModel(ctx, Map.of("location", location)));
    }

    public void updateStockLocation(Context ctx) {
        requireCan(ctx, "stocklocations.update");
        UpdateStockLocationCommand command = new UpdateStockLocationCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                ctx.formParam("name"),
                ctx.formParam("description"),
                parseInteger(ctx.formParam("capacity")));

        try {
            updateStockLocationUseCase.execute(command);
            flashSuccess(ctx, "Stock location updated successfully.");
            ctx.redirect("/stock/locations");
        } catch (ValidationException e) {
            StockLocationDTO location = getStockLocationUseCase.execute(command.id());
            Map<String, Object> model = buildEditModel(ctx, Map.of("location", location));
            model.putAll(e.getFieldErrors());
            ctx.render("stock/locations/form", model);
        } catch (StockLocationNotFoundException e) {
            flashDanger(ctx, "Stock location not found.");
            ctx.redirect("/stock/locations");
        }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Map<String, Object> buildEditModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> sections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "edit");
        model.put("user", current);
        model.put("navSections", sections);
        model.putAll(extra);
        return model;
    }
}
