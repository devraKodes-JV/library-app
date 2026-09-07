package com.library.stock.application.dto.command.stocklocation;

public record UpdateStockLocationCommand(
        Long id,
        String name,
        String description,
        Integer capacity) {
}
