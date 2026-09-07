package com.library.stock.infrastructure.web.controller.stockitem;

import java.util.List;
import java.util.Map;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.StockItemDTO;
import com.library.stock.application.dto.StockLocationDTO;
import com.library.stock.application.dto.command.stockitem.UpdateStockItemCommand;
import com.library.stock.application.service.stockitem.GetStockItemUseCase;
import com.library.stock.application.service.stockitem.UpdateStockItemUseCase;
import com.library.stock.application.service.stocklocation.ListStockLocationsUseCase;
import com.library.stock.domain.exception.StockItemNotFoundException;
import com.library.stock.domain.exception.ValidationException;
import com.library.stock.infrastructure.web.EditionNamesProvider;

import io.javalin.http.Context;

public class UpdateStockItemController extends BaseController {

    private final UpdateStockItemUseCase updateStockItemUseCase;
    private final GetStockItemUseCase getStockItemUseCase;
    private final EditionNamesProvider editionNamesProvider;
    private final ListStockLocationsUseCase listStockLocationsUseCase;

    public UpdateStockItemController(UpdateStockItemUseCase updateStockItemUseCase,
                                     GetStockItemUseCase getStockItemUseCase,
                                     EditionNamesProvider editionNamesProvider,
                                     ListStockLocationsUseCase listStockLocationsUseCase,
                                     WebControllerContext webContext) {
        super(webContext);
        this.updateStockItemUseCase = updateStockItemUseCase;
        this.getStockItemUseCase = getStockItemUseCase;
        this.editionNamesProvider = editionNamesProvider;
        this.listStockLocationsUseCase = listStockLocationsUseCase;
    }

    public void showEditForm(Context ctx) {
        requireCan(ctx, "stock.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        StockItemDTO item;
        try {
            item = getStockItemUseCase.execute(id);
        } catch (StockItemNotFoundException e) {
            throw new io.javalin.http.NotFoundResponse("StockItem not found");
        }
        Map<Long, String> editions = editionNamesProvider.getEditionNames("all");
        List<StockLocationDTO> locations = listStockLocationsUseCase.execute("all");
        ctx.render("stock/items/form", buildEditModel(ctx, Map.of(
                "item", item,
                "editions", editions,
                "locations", locations)));
    }

    public void updateStockItem(Context ctx) {
        requireCan(ctx, "stock.update");
        UpdateStockItemCommand command = new UpdateStockItemCommand(
                ctx.pathParamAsClass("id", Long.class).get(),
                parseLong(ctx.formParam("editionId")),
                parseLong(ctx.formParam("locationId")),
                ctx.formParam("state"),
                ctx.formParam("condition"));

        try {
            updateStockItemUseCase.execute(command);
            flashSuccess(ctx, "Stock item updated successfully.");
            ctx.redirect("/stock/items");
        } catch (ValidationException e) {
            StockItemDTO item = getStockItemUseCase.execute(command.id());
            Map<Long, String> editions = editionNamesProvider.getEditionNames("all");
            List<StockLocationDTO> locations = listStockLocationsUseCase.execute("all");
            Map<String, Object> model = buildEditModel(ctx, Map.of(
                    "item", item,
                    "editions", editions,
                    "locations", locations));
            model.putAll(e.getFieldErrors());
            ctx.render("stock/items/form", model);
        } catch (StockItemNotFoundException e) {
            flashDanger(ctx, "Stock item not found.");
            ctx.redirect("/stock/items");
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Long.parseLong(value);
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
