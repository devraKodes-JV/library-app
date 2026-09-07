package com.library.stock.infrastructure.web;

import com.library.iam.infrastructure.notification.SseNotificationService;
import com.library.security.service.SecurityAuditService;
import com.library.stock.infrastructure.web.controller.stockitem.ChangeStockItemStateController;
import com.library.stock.infrastructure.web.controller.stockitem.CreateStockItemController;
import com.library.stock.infrastructure.web.controller.stockitem.DeleteStockItemController;
import com.library.stock.infrastructure.web.controller.stockitem.ListStockItemsController;
import com.library.stock.infrastructure.web.controller.stockitem.ReactivateStockItemController;
import com.library.stock.infrastructure.web.controller.stockitem.ShowStockItemController;
import com.library.stock.infrastructure.web.controller.stockitem.UpdateStockItemController;
import com.library.stock.infrastructure.web.controller.stocklocation.CreateStockLocationController;
import com.library.stock.infrastructure.web.controller.stocklocation.DeleteStockLocationController;
import com.library.stock.infrastructure.web.controller.stocklocation.ListStockLocationsController;
import com.library.stock.infrastructure.web.controller.stocklocation.ReactivateStockLocationController;
import com.library.stock.infrastructure.web.controller.stocklocation.ShowStockLocationController;
import com.library.stock.infrastructure.web.controller.stocklocation.UpdateStockLocationController;

import io.javalin.config.JavalinConfig;

public final class StockRoutes {

    private StockRoutes() {
    }

    public static void register(JavalinConfig config,
                                ListStockItemsController listStockItemsController,
                                ShowStockItemController showStockItemController,
                                CreateStockItemController createStockItemController,
                                UpdateStockItemController updateStockItemController,
                                DeleteStockItemController deleteStockItemController,
                                ReactivateStockItemController reactivateStockItemController,
                                ChangeStockItemStateController changeStockItemStateController,
                                ListStockLocationsController listStockLocationsController,
                                ShowStockLocationController showStockLocationController,
                                CreateStockLocationController createStockLocationController,
                                UpdateStockLocationController updateStockLocationController,
                                DeleteStockLocationController deleteStockLocationController,
                                ReactivateStockLocationController reactivateStockLocationController,
                                SseNotificationService notificationService,
                                SecurityAuditService auditService) {

        config.routes.get("/stock/items", listStockItemsController::listStockItems);
        config.routes.get("/stock/items/new", createStockItemController::showCreateForm);
        config.routes.post("/stock/items", createStockItemController::createStockItem);
        config.routes.get("/stock/items/{id}", showStockItemController::showStockItem);
        config.routes.get("/stock/items/{id}/edit", updateStockItemController::showEditForm);
        config.routes.post("/stock/items/{id}", updateStockItemController::updateStockItem);
        config.routes.post("/stock/items/{id}/delete", deleteStockItemController::deleteStockItem);
        config.routes.post("/stock/items/{id}/reactivate", reactivateStockItemController::reactivateStockItem);
        config.routes.post("/stock/items/{id}/change-state", changeStockItemStateController::changeState);

        config.routes.get("/stock/locations", listStockLocationsController::listStockLocations);
        config.routes.get("/stock/locations/new", createStockLocationController::showCreateForm);
        config.routes.post("/stock/locations", createStockLocationController::createStockLocation);
        config.routes.get("/stock/locations/{id}", showStockLocationController::showStockLocation);
        config.routes.get("/stock/locations/{id}/edit", updateStockLocationController::showEditForm);
        config.routes.post("/stock/locations/{id}", updateStockLocationController::updateStockLocation);
        config.routes.post("/stock/locations/{id}/delete", deleteStockLocationController::deleteStockLocation);
        config.routes.post("/stock/locations/{id}/reactivate", reactivateStockLocationController::reactivateStockLocation);
    }
}
