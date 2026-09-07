package com.library.stock.application.service.stocklocation;

import java.util.List;

import com.library.stock.application.dto.StockLocationDTO;
import com.library.stock.domain.port.out.StockLocationRepository;

public class ListStockLocationsUseCase {

    private final StockLocationRepository stockLocationRepository;

    public ListStockLocationsUseCase(StockLocationRepository stockLocationRepository) {
        this.stockLocationRepository = stockLocationRepository;
    }

    public List<StockLocationDTO> execute() {
        return execute("active");
    }

    public List<StockLocationDTO> execute(String status) {
        return stockLocationRepository.findAll(status).stream()
                .map(StockLocationDTO::of)
                .toList();
    }
}
