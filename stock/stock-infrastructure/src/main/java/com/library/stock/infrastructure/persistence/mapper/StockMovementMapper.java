package com.library.stock.infrastructure.persistence.mapper;

import com.library.stock.domain.model.StockMovement;
import com.library.stock.infrastructure.persistence.entity.StockMovementEntity;

public final class StockMovementMapper {

    private StockMovementMapper() {
    }

    public static StockMovement toDomain(StockMovementEntity e) {
        if (e == null) {
            return null;
        }
        return new StockMovement(
                e.getId(),
                e.getType(),
                e.getStockItemId(),
                e.getSourceLocationId(),
                e.getTargetLocationId(),
                e.getReason(),
                e.getCreatedAt(),
                e.getCreatedBy());
    }

    public static StockMovementEntity toEntity(StockMovement m) {
        if (m == null) {
            return null;
        }
        return new StockMovementEntity(
                m.getId(),
                m.getType(),
                m.getStockItemId(),
                m.getSourceLocationId(),
                m.getTargetLocationId(),
                m.getReason(),
                m.getCreatedAt(),
                m.getCreatedBy());
    }
}
