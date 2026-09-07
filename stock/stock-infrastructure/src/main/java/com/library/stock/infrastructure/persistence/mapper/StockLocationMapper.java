package com.library.stock.infrastructure.persistence.mapper;

import com.library.stock.domain.model.StockLocation;
import com.library.stock.infrastructure.persistence.entity.StockLocationEntity;

public final class StockLocationMapper {

    private StockLocationMapper() {
    }

    public static StockLocation toDomain(StockLocationEntity e) {
        if (e == null) {
            return null;
        }
        return new StockLocation(
                e.getId(),
                e.getCode(),
                e.getName(),
                e.getDescription(),
                e.getCapacity(),
                e.getCreatedAt(),
                e.getUpdatedAt());
    }

    public static StockLocationEntity toEntity(StockLocation l) {
        if (l == null) {
            return null;
        }
        return new StockLocationEntity(
                l.getId(),
                l.getCode(),
                l.getName(),
                l.getDescription(),
                l.getCapacity(),
                l.isEnabled());
    }
}
