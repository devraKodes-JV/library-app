package com.library.stock.application.service.stockitem;

import java.util.Map;

import com.library.stock.application.dto.StockItemDTO;
import com.library.stock.application.dto.command.stockitem.UpdateStockItemCommand;
import com.library.stock.domain.model.StockItem;
import com.library.stock.domain.model.StockItemCondition;
import com.library.stock.domain.model.StockItemState;
import com.library.stock.domain.exception.StockItemNotFoundException;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.exception.ValidationException;

public class UpdateStockItemUseCase {

    private final StockItemRepository stockItemRepository;

    public UpdateStockItemUseCase(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    public StockItemDTO execute(UpdateStockItemCommand command) {
        StockItem existing = stockItemRepository.findById(command.id())
                .orElseThrow(() -> new StockItemNotFoundException(command.id()));

        if (command.editionId() != null) {
            existing.setEditionId(command.editionId());
        }
        if (command.locationId() != null) {
            existing.setLocationId(command.locationId());
        }
        if (command.state() != null) {
            existing.setState(StockItemState.valueOf(command.state()));
        }
        if (command.condition() != null) {
            existing.setCondition(StockItemCondition.fromLabel(command.condition()));
        }

        StockItem saved = stockItemRepository.save(existing);
        return StockItemDTO.of(saved);
    }
}
