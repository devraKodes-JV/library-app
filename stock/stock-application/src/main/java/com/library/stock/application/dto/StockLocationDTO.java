package com.library.stock.application.dto;

import java.time.Instant;

import com.library.stock.domain.model.StockLocation;

public record StockLocationDTO(
        Long id,
        String code,
        String name,
        String description,
        Integer capacity,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt) {

    public static StockLocationDTO of(StockLocation location) {
        return new StockLocationDTO(
                location.getId(),
                location.getCode(),
                location.getName(),
                location.getDescription(),
                location.getCapacity(),
                location.isEnabled(),
                location.getCreatedAt(),
                location.getUpdatedAt());
    }
}
