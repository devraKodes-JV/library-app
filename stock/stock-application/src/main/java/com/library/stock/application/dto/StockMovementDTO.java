package com.library.stock.application.dto;

import java.time.Instant;

import com.library.stock.domain.model.MovementType;
import com.library.stock.domain.model.StockMovement;

public record StockMovementDTO(
        Long id,
        MovementType type,
        String typeLabel,
        Long stockItemId,
        Long sourceLocationId,
        Long targetLocationId,
        String reason,
        Instant createdAt,
        String createdBy) {

    public static StockMovementDTO of(StockMovement movement) {
        return new StockMovementDTO(
                movement.getId(),
                movement.getType(),
                movement.getType() != null ? movement.getType().getLabel() : null,
                movement.getStockItemId(),
                movement.getSourceLocationId(),
                movement.getTargetLocationId(),
                movement.getReason(),
                movement.getCreatedAt(),
                movement.getCreatedBy());
    }
}
