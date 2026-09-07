package com.library.stock.infrastructure.persistence.adapter;

import java.util.List;

import com.library.stock.domain.model.StockMovement;
import com.library.stock.domain.port.out.StockMovementRepository;
import com.library.stock.infrastructure.persistence.entity.StockMovementEntity;
import com.library.stock.infrastructure.persistence.mapper.StockMovementMapper;
import com.library.stock.infrastructure.persistence.repository.hibernate.HibernateStockMovementRepository;

public class StockMovementPersistenceAdapter implements StockMovementRepository {

    private final HibernateStockMovementRepository hibernateStockMovementRepository;

    public StockMovementPersistenceAdapter(HibernateStockMovementRepository hibernateStockMovementRepository) {
        this.hibernateStockMovementRepository = hibernateStockMovementRepository;
    }

    @Override
    public List<StockMovement> findAll() {
        return hibernateStockMovementRepository.findAll().stream()
                .map(StockMovementMapper::toDomain)
                .toList();
    }

    @Override
    public List<StockMovement> findByStockItemId(Long stockItemId) {
        return hibernateStockMovementRepository.findByStockItemId(stockItemId).stream()
                .map(StockMovementMapper::toDomain)
                .toList();
    }

    @Override
    public StockMovement save(StockMovement movement) {
        StockMovementEntity entity = StockMovementMapper.toEntity(movement);
        StockMovementEntity saved = hibernateStockMovementRepository.save(entity);
        return StockMovementMapper.toDomain(saved);
    }
}
