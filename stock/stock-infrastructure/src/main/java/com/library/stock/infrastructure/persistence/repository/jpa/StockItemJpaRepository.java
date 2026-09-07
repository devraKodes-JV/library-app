package com.library.stock.infrastructure.persistence.repository.jpa;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.kernel.jpa.CrudRepository;
import com.library.stock.infrastructure.persistence.entity.StockItemEntity;

public interface StockItemJpaRepository<T, ID> extends CrudRepository<T, ID> {
    Optional<StockItemEntity> findByCode(String code);
    Optional<StockItemEntity> findByIdIncludingDeleted(Long id);
    List<StockItemEntity> findAll(String status);
    List<StockItemEntity> findByEditionId(Long editionId);
    List<StockItemEntity> findByLocationId(Long editionId);
    List<StockItemEntity> findAvailableByEditionId(Long editionId);
    Optional<StockItemEntity> findByReservationId(Long reservationId);
    void softDeleteStockItemsByEditionId(Long editionId);
    void reactivateById(Long id);
    void setState(Long id, com.library.stock.domain.model.StockItemState state);
    void setReservationId(Long id, Long reservationId);
    Map<Long, Integer> countByEditionId(List<Long> editionIds);
    List<StockItemEntity> findByIds(List<Long> ids);
}
