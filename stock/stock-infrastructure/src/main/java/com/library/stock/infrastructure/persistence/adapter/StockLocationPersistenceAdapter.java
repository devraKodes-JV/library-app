package com.library.stock.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.stock.domain.model.StockLocation;
import com.library.stock.domain.port.out.StockLocationRepository;
import com.library.stock.infrastructure.persistence.entity.StockLocationEntity;
import com.library.stock.infrastructure.persistence.mapper.StockLocationMapper;
import com.library.stock.infrastructure.persistence.repository.hibernate.HibernateStockLocationRepository;

public class StockLocationPersistenceAdapter implements StockLocationRepository {

    private final HibernateStockLocationRepository hibernateStockLocationRepository;

    public StockLocationPersistenceAdapter(HibernateStockLocationRepository hibernateStockLocationRepository) {
        this.hibernateStockLocationRepository = hibernateStockLocationRepository;
    }

    @Override
    public Optional<StockLocation> findById(Long id) {
        return hibernateStockLocationRepository.findById(id)
                .map(StockLocationMapper::toDomain);
    }

    @Override
    public Optional<StockLocation> findByCode(String code) {
        return hibernateStockLocationRepository.findByCode(code)
                .map(StockLocationMapper::toDomain);
    }

    @Override
    public List<StockLocation> findAll() {
        return hibernateStockLocationRepository.findAll().stream()
                .map(StockLocationMapper::toDomain)
                .toList();
    }

    @Override
    public List<StockLocation> findAll(String status) {
        return hibernateStockLocationRepository.findAll(status).stream()
                .map(StockLocationMapper::toDomain)
                .toList();
    }

    @Override
    public StockLocation save(StockLocation stockLocation) {
        StockLocationEntity entity = StockLocationMapper.toEntity(stockLocation);
        StockLocationEntity saved = hibernateStockLocationRepository.save(entity);
        return StockLocationMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        hibernateStockLocationRepository.deleteById(id);
    }

    @Override
    public void reactivateById(Long id) {
        hibernateStockLocationRepository.reactivateById(id);
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        return hibernateStockLocationRepository.findNamesByIds(ids);
    }

    @Override
    public long countStockItemsByLocationId(Long locationId) {
        return hibernateStockLocationRepository.countStockItemsByLocationId(locationId);
    }
}
