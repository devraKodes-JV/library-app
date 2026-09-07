package com.library.stock.application.service.stockmovement;

import java.util.Map;

import com.library.stock.application.dto.StockMovementDTO;
import com.library.stock.application.dto.command.stockmovement.CreateStockMovementCommand;
import com.library.stock.domain.exception.StockItemNotFoundException;
import com.library.stock.domain.exception.ValidationException;
import com.library.stock.domain.model.MovementType;
import com.library.stock.domain.model.StockMovement;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.port.out.StockMovementRepository;

public class CreateStockMovementUseCase {

    private final StockItemRepository stockItemRepository;
    private final StockMovementRepository stockMovementRepository;

    public CreateStockMovementUseCase(StockItemRepository stockItemRepository,
                                      StockMovementRepository stockMovementRepository) {
        this.stockItemRepository = stockItemRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public StockMovementDTO execute(CreateStockMovementCommand command) {
        MovementType type = MovementType.valueOf(command.type());

        if (stockItemRepository.findById(command.stockItemId()).isEmpty()) {
            throw new StockItemNotFoundException(command.stockItemId());
        }

        if (type == MovementType.TRANSFER && command.sourceLocationId() == null) {
            throw new ValidationException(Map.of("sourceLocationId", "Source location is required for transfer"));
        }
        if (command.targetLocationId() == null) {
            throw new ValidationException(Map.of("targetLocationId", "Target location is required"));
        }
        if (type == MovementType.EXIT && command.targetLocationId() != null) {
            throw new ValidationException(Map.of("targetLocationId", "Target location must be empty for exit"));
        }

        StockMovement movement = StockMovement.withoutId(
                type, command.stockItemId(), command.sourceLocationId(),
                command.targetLocationId(), command.reason());
        StockMovement saved = stockMovementRepository.save(movement);
        return StockMovementDTO.of(saved);
    }
}
