package com.library.stock.infrastructure.web.controller.stocklocation;

import java.util.List;
import java.util.Map;

import com.library.kernel.web.BaseController;
import com.library.kernel.web.WebControllerContext;
import com.library.stock.application.dto.command.stocklocation.CreateStockLocationCommand;
import com.library.stock.application.service.stocklocation.CreateStockLocationUseCase;
import com.library.stock.domain.exception.ValidationException;

import io.javalin.http.Context;

public class CreateStockLocationController extends BaseController {

    private final CreateStockLocationUseCase createStockLocationUseCase;

    public CreateStockLocationController(CreateStockLocationUseCase createStockLocationUseCase,
                                         WebControllerContext webContext) {
        super(webContext);
        this.createStockLocationUseCase = createStockLocationUseCase;
    }

    public void showCreateForm(Context ctx) {
        requireCan(ctx, "stocklocations.create");
        ctx.render("stock/locations/form", buildCreateModel(ctx));
    }

    public void createStockLocation(Context ctx) {
        requireCan(ctx, "stocklocations.create");
        CreateStockLocationCommand command = new CreateStockLocationCommand(
                ctx.formParam("name"),
                ctx.formParam("description"),
                parseInteger(ctx.formParam("capacity")));

        try {
            createStockLocationUseCase.execute(command);
            flashSuccess(ctx, "Stock location created successfully.");
            ctx.redirect("/stock/locations");
        } catch (ValidationException e) {
            Map<String, Object> model = buildCreateModel(ctx);
            model.put("validationError", true);
            model.putAll(e.getFieldErrors());
            ctx.render("stock/locations/form", model);
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

    private Map<String, Object> buildCreateModel(Context ctx) {
        var current = currentUser(ctx);
        List<?> sections = navSections(ctx);
        Map<String, Object> model = new java.util.LinkedHashMap<>();
        model.put("mode", "create");
        model.put("user", current);
        model.put("navSections", sections);
        return model;
    }
}
