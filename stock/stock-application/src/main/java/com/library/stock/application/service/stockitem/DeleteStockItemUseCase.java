package com.library.stock.application.service.stockitem;

import com.library.stock.application.dto.command.stockitem.DeleteStockItemCommand;
import com.library.stock.domain.exception.StockItemNotFoundException;
import com.library.stock.domain.port.out.StockItemRepository;

public class DeleteStockItemUseCase {

    private final StockItemRepository stockItemRepository;

    public DeleteStockItemUseCase(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public void execute(DeleteStockItemCommand command) {
        if (stockItemRepository.findById(command.id()).isEmpty()) {
            throw new StockItemNotFoundException(command.id());
        }
        stockItemRepository.deleteById(command.id());
    }
}
