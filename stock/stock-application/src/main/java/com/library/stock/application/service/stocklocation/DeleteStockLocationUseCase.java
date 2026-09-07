package com.library.stock.application.service.stocklocation;

import com.library.stock.application.dto.command.stocklocation.DeleteStockLocationCommand;
import com.library.stock.domain.exception.StockLocationNotFoundException;
import com.library.stock.domain.port.out.StockLocationRepository;

public class DeleteStockLocationUseCase {

    private final StockLocationRepository stockLocationRepository;

    public DeleteStockLocationUseCase(StockLocationRepository stockLocationRepository) {
        this.stockLocationRepository = stockLocationRepository;
    }

    public void execute(DeleteStockLocationCommand command) {
        if (stockLocationRepository.findById(command.id()).isEmpty()) {
            throw new StockLocationNotFoundException(command.id());
        }
        if (stockLocationRepository.countStockItemsByLocationId(command.id()) > 0) {
            throw new IllegalStateException("Cannot delete location with stock items. Move items first.");
        }
        stockLocationRepository.deleteById(command.id());
    }
}
