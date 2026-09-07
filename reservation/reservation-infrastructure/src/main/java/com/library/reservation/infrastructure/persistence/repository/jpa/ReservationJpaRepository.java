package com.library.reservation.infrastructure.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import com.library.kernel.jpa.CrudRepository;
import com.library.reservation.domain.model.ReservationStatus;
import com.library.reservation.infrastructure.persistence.entity.ReservationEntity;

public interface ReservationJpaRepository<T, ID> extends CrudRepository<T, ID> {
    Optional<ReservationEntity> findByCode(String code);
    Optional<ReservationEntity> findByIdIncludingDeleted(Long id);
    List<ReservationEntity> findAll(String status);
    List<ReservationEntity> findByClientId(Long clientId);
    List<ReservationEntity> findByEditionId(Long editionId);
    List<ReservationEntity> findByStatus(ReservationStatus status);
    long countActiveByClientId(Long clientId);
    void reactivateById(Long id);
}
