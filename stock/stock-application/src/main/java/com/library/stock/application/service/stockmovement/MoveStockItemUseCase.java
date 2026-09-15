package com.library.stock.application.service.stockmovement;

import com.library.stock.application.dto.command.stockmovement.CreateStockMovementCommand;
import com.library.stock.domain.exception.StockItemNotFoundException;
import com.library.stock.domain.model.MovementType;
import com.library.stock.domain.model.StockMovement;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.domain.port.out.StockMovementRepository;

public class MoveStockItemUseCase {

    private final StockItemRepository stockItemRepository;
    private final StockMovementRepository stockMovementRepository;

    public MoveStockItemUseCase(StockItemRepository stockItemRepository,
                                StockMovementRepository stockMovementRepository) {
        this.stockItemRepository = stockItemRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    public void execute(Long stockItemId, Long targetLocationId, String reason, String movedBy) {
        var item = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new StockItemNotFoundException(stockItemId));
        Long sourceLocationId = item.getLocationId();
        item.setLocationId(targetLocationId);
        stockItemRepository.save(item);

        StockMovement movement = StockMovement.withoutId(
                MovementType.TRANSFER, stockItemId, sourceLocationId,
                targetLocationId, reason);
        movement.setCreatedBy(movedBy);
        stockMovementRepository.save(movement);
    }

    public void execute(CreateStockMovementCommand command, String movedBy) {
        execute(command.stockItemId(), command.targetLocationId(),
                command.reason(), movedBy);
    }
}
