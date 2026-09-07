package com.library.stock.application.dto.command.stocklocation;

public record CreateStockLocationCommand(
        String name,
        String description,
        Integer capacity) {
}
