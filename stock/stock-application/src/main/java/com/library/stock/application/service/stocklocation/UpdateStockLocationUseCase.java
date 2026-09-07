package com.library.stock.application.service.stocklocation;

import com.library.stock.application.dto.StockLocationDTO;
import com.library.stock.application.dto.command.stocklocation.UpdateStockLocationCommand;
import com.library.stock.domain.exception.StockLocationNotFoundException;
import com.library.stock.domain.model.StockLocation;
import com.library.stock.domain.port.out.StockLocationRepository;

public class UpdateStockLocationUseCase {

    private final StockLocationRepository stockLocationRepository;

    public UpdateStockLocationUseCase(StockLocationRepository stockLocationRepository) {
        this.stockLocationRepository = stockLocationRepository;
    }

    public StockLocationDTO execute(UpdateStockLocationCommand command) {
        StockLocation existing = stockLocationRepository.findById(command.id())
                .orElseThrow(() -> new StockLocationNotFoundException(command.id()));

        if (command.name() != null && !command.name().isBlank()) {
            existing.setName(command.name());
        }
        if (command.description() != null) {
            existing.setDescription(command.description());
        }
        if (command.capacity() != null) {
            existing.setCapacity(command.capacity());
        }

        StockLocation saved = stockLocationRepository.save(existing);
        return StockLocationDTO.of(saved);
    }
}
