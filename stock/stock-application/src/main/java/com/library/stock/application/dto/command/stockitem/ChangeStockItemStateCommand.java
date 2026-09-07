package com.library.stock.application.dto.command.stockitem;

public record ChangeStockItemStateCommand(
        Long id,
        String state) {
}
