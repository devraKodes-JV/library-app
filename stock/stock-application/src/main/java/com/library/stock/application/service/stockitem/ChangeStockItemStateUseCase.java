package com.library.stock.application.service.stockitem;

import com.library.stock.application.dto.command.stockitem.ChangeStockItemStateCommand;
import com.library.stock.domain.exception.StockItemNotFoundException;
import com.library.stock.domain.model.StockItemState;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.port.out.StockNotificationService;
import com.library.stock.domain.model.NotificationEvent;

public class ChangeStockItemStateUseCase {

    private final StockItemRepository stockItemRepository;
    private final StockNotificationService notificationService;

    public ChangeStockItemStateUseCase(StockItemRepository stockItemRepository, StockNotificationService notificationService) {
        this.stockItemRepository = stockItemRepository;
        this.notificationService = notificationService;
    }

    public void execute(ChangeStockItemStateCommand command) {
        StockItemState newState = StockItemState.valueOf(command.state());

        stockItemRepository.setState(command.id(), newState);

        if (newState == StockItemState.REVIEW) {
            notificationService.publish(NotificationEvent.of(
                    "stockitem.returned",
                    "StockItem " + command.id() + " has been returned and is pending review",
                    null,
                    null));
        } else if (newState == StockItemState.BORROWED) {
            notificationService.publish(NotificationEvent.of(
                    "stockitem.borrowed",
                    "StockItem " + command.id() + " has been borrowed",
                    null,
                    null));
        }
    }
}
