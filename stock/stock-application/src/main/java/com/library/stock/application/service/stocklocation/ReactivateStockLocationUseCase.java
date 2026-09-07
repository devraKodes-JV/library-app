package com.library.stock.application.service.stocklocation;

import com.library.stock.application.dto.command.stocklocation.ReactivateStockLocationCommand;
import com.library.stock.domain.exception.StockLocationNotFoundException;
import com.library.stock.domain.port.out.StockLocationRepository;

public class ReactivateStockLocationUseCase {

    private final StockLocationRepository stockLocationRepository;

    public ReactivateStockLocationUseCase(StockLocationRepository stockLocationRepository) {
        this.stockLocationRepository = stockLocationRepository;
    }

    public void execute(ReactivateStockLocationCommand command) {
        if (stockLocationRepository.findById(command.id()).isPresent()) {
            return;
        }
        stockLocationRepository.reactivateById(command.id());
    }
}
