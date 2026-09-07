package com.library.client.infrastructure.persistence.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.client.domain.model.Client;
import com.library.client.domain.port.out.ClientRepository;
import com.library.client.infrastructure.persistence.entity.ClientEntity;
import com.library.client.infrastructure.persistence.mapper.ClientMapper;
import com.library.client.infrastructure.persistence.repository.hibernate.HibernateClientRepository;
import com.library.client.infrastructure.persistence.repository.jpa.ClientJpaRepository;

public class ClientPersistenceAdapter implements ClientRepository {

    private final ClientJpaRepository<ClientEntity, Long> clientJpaRepository;
    private final HibernateClientRepository hibernateRepository;

    public ClientPersistenceAdapter(ClientJpaRepository<ClientEntity, Long> clientJpaRepository,
                                    HibernateClientRepository hibernateRepository) {
        this.clientJpaRepository = clientJpaRepository;
        this.hibernateRepository = hibernateRepository;
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientJpaRepository.findById(id)
                .map(ClientMapper::toDomain);
    }

    @Override
    public Optional<Client> findByIdIncludingDeleted(Long id) {
        return clientJpaRepository.findByIdIncludingDeleted(id)
                .map(ClientMapper::toDomain);
    }

    @Override
    public Optional<Client> findByCode(String code) {
        return clientJpaRepository.findByCode(code)
                .map(ClientMapper::toDomain);
    }

    @Override
    public Optional<Client> findByDni(String dni) {
        return clientJpaRepository.findByDni(dni)
                .map(ClientMapper::toDomain);
    }

    @Override
    public List<Client> findAll() {
        return clientJpaRepository.findAll().stream()
                .map(ClientMapper::toDomain)
                .toList();
    }

    @Override
    public List<Client> findAll(String status) {
        return clientJpaRepository.findAll(status).stream()
                .map(ClientMapper::toDomain)
                .toList();
    }

    @Override
    public Client save(Client client) {
        ClientEntity entity = ClientMapper.toEntity(client);
        ClientEntity saved = clientJpaRepository.save(entity);
        return ClientMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        clientJpaRepository.deleteById(id);
    }

    @Override
    public void reactivateById(Long id) {
        clientJpaRepository.reactivateById(id);
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        return clientJpaRepository.findNamesByIds(ids);
    }

    @Override
    public int countActiveByTypeAndStatus(String type, String status) {
        return clientJpaRepository.countActiveByTypeAndStatus(type, status);
    }

    @Override
    public long countActiveReservations(Long clientId) {
        return clientJpaRepository.countActiveReservations(clientId);
    }

    @Override
    public List<Client> findExpiredUnpaidMembers(LocalDate asOf) {
        return hibernateRepository.findExpiredUnpaidMembers(asOf);
    }
}
