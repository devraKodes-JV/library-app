package com.library.stock.infrastructure.persistence.repository.jpa;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByCode;
import com.library.stock.infrastructure.persistence.entity.StockLocationEntity;

public interface StockLocationJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByCode<T> {
    Optional<StockLocationEntity> findByIdIncludingDeleted(Long id);
    List<StockLocationEntity> findAll(String status);
    void reactivateById(Long id);
    Map<Long, String> findNamesByIds(List<Long> ids);
    long countStockItemsByLocationId(Long locationId);
}
