package com.library.stock.application.service.stocklocation;

import com.library.stock.application.dto.StockLocationDTO;
import com.library.stock.domain.exception.StockLocationNotFoundException;
import com.library.stock.domain.port.out.StockLocationRepository;

public class GetStockLocationUseCase {

    private final StockLocationRepository stockLocationRepository;

    public GetStockLocationUseCase(StockLocationRepository stockLocationRepository) {
        this.stockLocationRepository = stockLocationRepository;
    }

    public StockLocationDTO execute(Long id) {
        return stockLocationRepository.findById(id)
                .map(StockLocationDTO::of)
                .orElseThrow(() -> new StockLocationNotFoundException(id));
    }
}
