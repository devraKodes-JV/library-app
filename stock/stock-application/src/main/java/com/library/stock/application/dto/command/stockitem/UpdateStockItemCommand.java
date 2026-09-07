package com.library.stock.application.dto.command.stockitem;

public record UpdateStockItemCommand(
        Long id,
        Long editionId,
        Long locationId,
        String state,
        String condition) {
}
