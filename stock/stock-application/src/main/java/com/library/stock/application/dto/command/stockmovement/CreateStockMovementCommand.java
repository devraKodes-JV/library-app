package com.library.stock.application.dto.command.stockmovement;

public record CreateStockMovementCommand(
        String type,
        Long stockItemId,
        Long sourceLocationId,
        Long targetLocationId,
        String reason) {
}
