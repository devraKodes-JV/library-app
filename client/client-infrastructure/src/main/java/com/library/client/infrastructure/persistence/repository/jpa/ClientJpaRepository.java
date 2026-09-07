package com.library.client.infrastructure.persistence.repository.jpa;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.kernel.jpa.CrudRepository;
import com.library.client.infrastructure.persistence.entity.ClientEntity;

public interface ClientJpaRepository<T, ID> extends CrudRepository<T, ID> {
    Optional<ClientEntity> findByCode(String code);
    Optional<ClientEntity> findByDni(String dni);
    Optional<ClientEntity> findByIdIncludingDeleted(Long id);
    List<ClientEntity> findAll(String status);
    void reactivateById(Long id);
    Map<Long, String> findNamesByIds(List<Long> ids);
    int countActiveByTypeAndStatus(String type, String status);
    long countActiveReservations(Long clientId);
}
