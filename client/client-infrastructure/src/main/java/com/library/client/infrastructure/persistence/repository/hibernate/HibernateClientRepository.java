package com.library.client.infrastructure.persistence.repository.hibernate;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.client.domain.model.Client;
import com.library.client.domain.model.ClientStatus;
import com.library.client.infrastructure.persistence.entity.ClientEntity;
import com.library.client.infrastructure.persistence.mapper.ClientMapper;
import com.library.client.infrastructure.persistence.repository.jpa.ClientJpaRepository;

public class HibernateClientRepository extends AbstractHibernateRepository implements ClientJpaRepository<ClientEntity, Long> {

    public HibernateClientRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<ClientEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            ClientEntity entity = session.createQuery(
                    "select c from ClientEntity c where c.id = :id and c.deletedAt is null",
                    ClientEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<ClientEntity> findByIdIncludingDeleted(Long id) {
        try (Session session = sessionFactory.openSession()) {
            ClientEntity entity = session.get(ClientEntity.class, id);
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<ClientEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            ClientEntity entity = session.createQuery(
                    "select c from ClientEntity c where c.code = :code and c.deletedAt is null",
                    ClientEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<ClientEntity> findByDni(String dni) {
        try (Session session = sessionFactory.openSession()) {
            ClientEntity entity = session.createQuery(
                    "select c from ClientEntity c where c.dni = :dni and c.deletedAt is null",
                    ClientEntity.class)
                    .setParameter("dni", dni)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public List<ClientEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select c from ClientEntity c where c.deletedAt is null and c.enabled = true order by c.code",
                    ClientEntity.class)
                    .getResultList();
        }
    }

    @Override
    public List<ClientEntity> findAll(String status) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select c from ClientEntity c";
            if ("active".equals(status)) {
                hql += " where c.deletedAt is null and c.enabled = true";
            } else if ("inactive".equals(status)) {
                hql += " where c.deletedAt is not null or c.enabled = false";
            }
            hql += " order by c.code";
            return session.createQuery(hql, ClientEntity.class).getResultList();
        }
    }

    @Override
    public ClientEntity save(ClientEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (ClientEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        executeWithSession(session -> {
            ClientEntity entity = session.get(ClientEntity.class, id);
            if (entity != null && entity.getDeletedAt() == null) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
    }

    @Override
    public void reactivateById(Long id) {
        consumeWithSession(session -> session.createMutationQuery(
                "update ClientEntity c set c.deletedAt = null, c.enabled = true where c.id = :id and c.deletedAt is not null")
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        try (Session session = sessionFactory.openSession()) {
            List<Object[]> results = session.createQuery(
                    "select c.id, c.fullName from ClientEntity c where c.id in :ids and c.deletedAt is null",
                    Object[].class)
                    .setParameter("ids", ids)
                    .getResultList();
            Map<Long, String> names = new HashMap<>();
            for (Object[] row : results) {
                names.put(((Number) row[0]).longValue(), (String) row[1]);
            }
            return names;
        }
    }

    @Override
    public int countActiveByTypeAndStatus(String type, String status) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                    "select count(c) from ClientEntity c where c.type = :type and c.status = :status and c.deletedAt is null and c.enabled = true",
                    Long.class)
                    .setParameter("type", type)
                    .setParameter("status", status)
                    .uniqueResult();
            return count != null ? count.intValue() : 0;
        }
    }

    @Override
    public long countActiveReservations(Long clientId) {
        return 0;
    }

    public List<Client> findExpiredUnpaidMembers(java.time.LocalDate asOf) {
        try (Session session = sessionFactory.openSession()) {
            List<ClientEntity> entities = session.createQuery(
                    "from ClientEntity where deletedAt is null and type = com.library.client.domain.model.ClientType.MEMBER " +
                    "and membershipPaid = false and memberUntil is not null and memberUntil < :asOf",
                    ClientEntity.class)
                    .setParameter("asOf", asOf)
                    .getResultList();
            return entities.stream().map(ClientMapper::toDomain).toList();
        }
    }
}
