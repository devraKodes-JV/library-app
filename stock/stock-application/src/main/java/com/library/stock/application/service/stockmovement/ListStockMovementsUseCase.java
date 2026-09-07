package com.library.stock.application.service.stockmovement;

import java.util.List;

import com.library.stock.application.dto.StockMovementDTO;
import com.library.stock.domain.port.out.StockMovementRepository;

public class ListStockMovementsUseCase {

    private final StockMovementRepository stockMovementRepository;

    public ListStockMovementsUseCase(StockMovementRepository stockMovementRepository) {
        this.stockMovementRepository = stockMovementRepository;
    }

    public List<StockMovementDTO> execute() {
        return stockMovementRepository.findAll().stream()
                .map(StockMovementDTO::of)
                .toList();
    }
}
