package com.library.stock.application.service.stockitem;

import com.library.stock.application.dto.command.stockitem.ReactivateStockItemCommand;
import com.library.stock.domain.exception.StockItemNotFoundException;
import com.library.stock.domain.port.out.StockItemRepository;

public class ReactivateStockItemUseCase {

    private final StockItemRepository stockItemRepository;

    public ReactivateStockItemUseCase(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public void execute(ReactivateStockItemCommand command) {
        if (stockItemRepository.findByIdIncludingDeleted(command.id()).isEmpty()) {
            throw new StockItemNotFoundException(command.id());
        }
        stockItemRepository.reactivateById(command.id());
    }
}
