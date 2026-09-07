package com.library.stock.infrastructure.web.controller.stockitem;

import java.util.List;
import java.util.Map;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.StockLocationDTO;
import com.library.stock.application.dto.command.stockitem.CreateStockItemCommand;
import com.library.stock.application.service.stockitem.CreateStockItemUseCase;
import com.library.stock.application.service.stocklocation.ListStockLocationsUseCase;
import com.library.stock.domain.exception.ValidationException;
import com.library.stock.infrastructure.web.EditionNamesProvider;

import io.javalin.http.Context;

public class CreateStockItemController extends BaseController {

    private final CreateStockItemUseCase createStockItemUseCase;
    private final EditionNamesProvider editionNamesProvider;
    private final ListStockLocationsUseCase listStockLocationsUseCase;

    public CreateStockItemController(CreateStockItemUseCase createStockItemUseCase,
                                     EditionNamesProvider editionNamesProvider,
                                     ListStockLocationsUseCase listStockLocationsUseCase,
                                     WebControllerContext webContext) {
        super(webContext);
        this.createStockItemUseCase = createStockItemUseCase;
        this.editionNamesProvider = editionNamesProvider;
        this.listStockLocationsUseCase = listStockLocationsUseCase;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "stock.create");
        Map<Long, String> editions = editionNamesProvider.getEditionNames("active");
        List<StockLocationDTO> locations = listStockLocationsUseCase.execute("active");
        ctx.render("stock/items/form", buildCreateModel(ctx, Map.of(
                "editions", editions,
                "locations", locations)));
    }

    public void createStockItem(Context ctx) {
        requireCan(ctx, "stock.create");
        CreateStockItemCommand command = new CreateStockItemCommand(
                parseLong(ctx.formParam("editionId")),
                parseLong(ctx.formParam("locationId")),
                ctx.formParam("state"),
                ctx.formParam("condition"),
                parseBigDecimal(ctx.formParam("dailyPrice")));

        try {
            createStockItemUseCase.execute(command);
            flashSuccess(ctx, "Stock item created successfully.");
            ctx.redirect("/stock/items");
        } catch (ValidationException e) {
            Map<Long, String> editions = editionNamesProvider.getEditionNames("active");
            List<StockLocationDTO> locations = listStockLocationsUseCase.execute("active");
            Map<String, Object> model = buildCreateModel(ctx, Map.of(
                    "editions", editions,
                    "locations", locations));
            model.put("validationError", true);
            model.putAll(e.getFieldErrors());
            ctx.render("stock/items/form", model);
        }
    }

    private Map<String, Object> buildCreateModel(Context ctx, Map<String, Object> extra) {
        var current = currentUser(ctx);
        List<?> sections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("user", current);
        model.put("navSections", sections);
        model.putAll(extra);
        return model;
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private java.math.BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return new java.math.BigDecimal(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
