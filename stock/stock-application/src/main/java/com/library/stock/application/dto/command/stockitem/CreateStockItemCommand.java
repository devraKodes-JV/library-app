package com.library.stock.application.dto.command.stockitem;

import java.math.BigDecimal;

public record CreateStockItemCommand(
        Long editionId,
        Long locationId,
        String state,
        String condition,
        BigDecimal dailyPrice) {
}
