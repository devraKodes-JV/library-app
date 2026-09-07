package com.library.stock.infrastructure.persistence.repository.jpa;

import java.util.List;

import com.library.stock.infrastructure.persistence.entity.StockMovementEntity;

public interface StockMovementJpaRepository {
    List<StockMovementEntity> findAll();
    List<StockMovementEntity> findByStockItemId(Long stockItemId);
    StockMovementEntity save(StockMovementEntity movement);
}
