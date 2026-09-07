package com.library.stock.application.dto;

import java.time.Instant;

import com.library.stock.domain.model.StockItem;
import com.library.stock.domain.model.StockItemCondition;
import com.library.stock.domain.model.StockItemState;

public record StockItemDTO(
        Long id,
        String code,
        Long editionId,
        String editionName,
        Long locationId,
        String locationName,
        StockItemState state,
        String stateLabel,
        StockItemCondition condition,
        String conditionLabel,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt) {

    public static StockItemDTO of(StockItem item) {
        return new StockItemDTO(
                item.getId(),
                item.getCode(),
                item.getEditionId(),
                null,
                item.getLocationId(),
                null,
                item.getState(),
                item.getState() != null ? item.getState().getLabel() : null,
                item.getCondition(),
                item.getCondition() != null ? item.getCondition().getLabel() : null,
                item.isEnabled(),
                item.getCreatedAt(),
                item.getUpdatedAt());
    }

    public static StockItemDTO of(StockItem item, String editionName, String locationName) {
        return new StockItemDTO(
                item.getId(),
                item.getCode(),
                item.getEditionId(),
                editionName != null ? editionName : "",
                item.getLocationId(),
                locationName != null ? locationName : "",
                item.getState(),
                item.getState() != null ? item.getState().getLabel() : null,
                item.getCondition(),
                item.getCondition() != null ? item.getCondition().getLabel() : null,
                item.isEnabled(),
                item.getCreatedAt(),
                item.getUpdatedAt());
    }
}
