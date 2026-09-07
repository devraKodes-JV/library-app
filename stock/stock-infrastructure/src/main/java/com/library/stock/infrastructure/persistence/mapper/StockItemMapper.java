package com.library.stock.infrastructure.persistence.mapper;

import com.library.stock.domain.model.StockItem;
import com.library.stock.infrastructure.persistence.entity.StockItemEntity;

public final class StockItemMapper {

    private StockItemMapper() {
    }

    public static StockItem toDomain(StockItemEntity e) {
        if (e == null) {
            return null;
        }
        return new StockItem(
                e.getId(),
                e.getCode(),
                e.getEditionId(),
                e.getLocationId(),
                e.getReservationId(),
                e.getState(),
                e.getCondition(),
                e.getDailyPrice(),
                e.getCreatedAt(),
                e.getUpdatedAt());
    }

    public static StockItemEntity toEntity(StockItem i) {
        if (i == null) {
            return null;
        }
        return new StockItemEntity(
                i.getId(),
                i.getCode(),
                i.getEditionId(),
                i.getLocationId(),
                i.getReservationId(),
                i.getState(),
                i.getCondition(),
                i.getDailyPrice(),
                i.isEnabled());
    }
}
