package com.library.stock.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.stock.domain.model.StockItem;
import com.library.stock.domain.model.StockItemState;
import com.library.stock.domain.port.out.StockItemRepository;
import com.library.stock.infrastructure.persistence.entity.StockItemEntity;
import com.library.stock.infrastructure.persistence.mapper.StockItemMapper;
import com.library.stock.infrastructure.persistence.repository.hibernate.HibernateStockItemRepository;

public class StockItemPersistenceAdapter implements StockItemRepository {

    private final HibernateStockItemRepository hibernateStockItemRepository;

    public StockItemPersistenceAdapter(HibernateStockItemRepository hibernateStockItemRepository) {
        this.hibernateStockItemRepository = hibernateStockItemRepository;
    }

    @Override
    public Optional<StockItem> findById(Long id) {
        return hibernateStockItemRepository.findById(id)
                .map(StockItemMapper::toDomain);
    }

    @Override
    public Optional<StockItem> findByIdIncludingDeleted(Long id) {
        return hibernateStockItemRepository.findByIdIncludingDeleted(id)
                .map(StockItemMapper::toDomain);
    }

    @Override
    public Optional<StockItem> findByCode(String code) {
        return hibernateStockItemRepository.findByCode(code)
                .map(StockItemMapper::toDomain);
    }

    @Override
    public List<StockItem> findAll() {
        return hibernateStockItemRepository.findAll().stream()
                .map(StockItemMapper::toDomain)
                .toList();
    }

    @Override
    public List<StockItem> findAll(String status) {
        return hibernateStockItemRepository.findAll(status).stream()
                .map(StockItemMapper::toDomain)
                .toList();
    }

    @Override
    public List<StockItem> findByEditionId(Long editionId) {
        return hibernateStockItemRepository.findByEditionId(editionId).stream()
                .map(StockItemMapper::toDomain)
                .toList();
    }

    @Override
    public List<StockItem> findByLocationId(Long locationId) {
        return hibernateStockItemRepository.findByLocationId(locationId).stream()
                .map(StockItemMapper::toDomain)
                .toList();
    }

    @Override
    public List<StockItem> findAvailableByEditionId(Long editionId) {
        return hibernateStockItemRepository.findAvailableByEditionId(editionId).stream()
                .map(StockItemMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<StockItem> findByReservationId(Long reservationId) {
        return hibernateStockItemRepository.findByReservationId(reservationId)
                .map(StockItemMapper::toDomain);
    }

    @Override
    public StockItem save(StockItem stockItem) {
        StockItemEntity entity = StockItemMapper.toEntity(stockItem);
        StockItemEntity saved = hibernateStockItemRepository.save(entity);
        return StockItemMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        hibernateStockItemRepository.deleteById(id);
    }

    @Override
    public void softDeleteStockItemsByEditionId(Long editionId) {
        hibernateStockItemRepository.softDeleteStockItemsByEditionId(editionId);
    }

    @Override
    public void reactivateById(Long id) {
        hibernateStockItemRepository.reactivateById(id);
    }

    @Override
    public void setState(Long id, StockItemState state) {
        hibernateStockItemRepository.setState(id, state);
    }

    @Override
    public void setReservationId(Long id, Long reservationId) {
        hibernateStockItemRepository.setReservationId(id, reservationId);
    }

    @Override
    public Map<Long, Integer> countByEditionId(List<Long> editionIds) {
        return hibernateStockItemRepository.countByEditionId(editionIds);
    }

    @Override
    public List<StockItem> findByIds(List<Long> ids) {
        return hibernateStockItemRepository.findByIds(ids).stream()
                .map(StockItemMapper::toDomain)
                .toList();
    }
}
