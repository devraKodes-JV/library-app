package com.library.stock.application.service.stockitem;

import java.util.Optional;

import com.library.stock.application.dto.StockItemDTO;
import com.library.stock.domain.exception.StockItemNotFoundException;
import com.library.stock.domain.model.StockItem;
import com.library.stock.domain.port.out.StockItemRepository;

public class GetStockItemUseCase {

    private final StockItemRepository stockItemRepository;

    public GetStockItemUseCase(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public StockItemDTO execute(Long id) {
        StockItem item = stockItemRepository.findById(id)
                .orElseThrow(() -> new StockItemNotFoundException(id));
        return StockItemDTO.of(item);
    }
}
