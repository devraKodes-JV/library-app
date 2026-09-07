package com.library.stock.infrastructure.persistence.repository.hibernate;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.stock.domain.model.StockItemState;
import com.library.stock.infrastructure.persistence.entity.StockItemEntity;
import com.library.stock.infrastructure.persistence.repository.jpa.StockItemJpaRepository;

public class HibernateStockItemRepository extends AbstractHibernateRepository implements StockItemJpaRepository<StockItemEntity, Long> {

    public HibernateStockItemRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<StockItemEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            StockItemEntity entity = session.createQuery(
                    "select s from StockItemEntity s where s.id = :id and s.deletedAt is null",
                    StockItemEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<StockItemEntity> findByIdIncludingDeleted(Long id) {
        try (Session session = sessionFactory.openSession()) {
            StockItemEntity entity = session.createQuery(
                    "select s from StockItemEntity s where s.id = :id",
                    StockItemEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<StockItemEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            StockItemEntity entity = session.createQuery(
                    "select s from StockItemEntity s where s.code = :code and s.deletedAt is null",
                    StockItemEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public List<StockItemEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select s from StockItemEntity s where s.deletedAt is null and s.enabled = true order by s.code",
                    StockItemEntity.class)
                    .getResultList();
        }
    }

    @Override
    public List<StockItemEntity> findAll(String status) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select s from StockItemEntity s";
            if ("active".equals(status)) {
                hql += " where s.deletedAt is null and s.enabled = true";
            } else if ("inactive".equals(status)) {
                hql += " where s.deletedAt is not null or s.enabled = false";
            }
            hql += " order by s.code";
            return session.createQuery(hql, StockItemEntity.class).getResultList();
        }
    }

    @Override
    public List<StockItemEntity> findByEditionId(Long editionId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select s from StockItemEntity s where s.editionId = :editionId and s.deletedAt is null order by s.code",
                    StockItemEntity.class)
                    .setParameter("editionId", editionId)
                    .getResultList();
        }
    }

    @Override
    public List<StockItemEntity> findByLocationId(Long locationId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select s from StockItemEntity s where s.locationId = :locationId and s.deletedAt is null order by s.code",
                    StockItemEntity.class)
                    .setParameter("locationId", locationId)
                    .getResultList();
        }
    }

    @Override
    public StockItemEntity save(StockItemEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (StockItemEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        executeWithSession(session -> {
            StockItemEntity entity = session.get(StockItemEntity.class, id);
            if (entity != null && entity.getDeletedAt() == null) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
    }

    @Override
    public void softDeleteStockItemsByEditionId(Long editionId) {
        consumeWithSession(session -> session.createMutationQuery(
                "update StockItemEntity s set s.deletedAt = :now, s.enabled = false " +
                "where s.editionId = :editionId and s.deletedAt is null and s.enabled = true")
                .setParameter("now", Instant.now())
                .setParameter("editionId", editionId)
                .executeUpdate());
    }

    @Override
    public void reactivateById(Long id) {
        consumeWithSession(session -> session.createMutationQuery(
                "update StockItemEntity s set s.deletedAt = null, s.enabled = true where s.id = :id and s.deletedAt is not null")
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public void setState(Long id, StockItemState state) {
        consumeWithSession(session -> session.createMutationQuery(
                "update StockItemEntity s set s.state = :state where s.id = :id")
                .setParameter("state", state)
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public void setReservationId(Long id, Long reservationId) {
        consumeWithSession(session -> session.createMutationQuery(
                "update StockItemEntity s set s.reservationId = :reservationId where s.id = :id")
                .setParameter("reservationId", reservationId)
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public List<StockItemEntity> findAvailableByEditionId(Long editionId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select s from StockItemEntity s where s.editionId = :editionId " +
                    "and s.state = :state and s.deletedAt is null and s.enabled = true " +
                    "order by s.code",
                    StockItemEntity.class)
                    .setParameter("editionId", editionId)
                    .setParameter("state", StockItemState.AVAILABLE)
                    .getResultList();
        }
    }

    @Override
    public Optional<StockItemEntity> findByReservationId(Long reservationId) {
        try (Session session = sessionFactory.openSession()) {
            StockItemEntity entity = session.createQuery(
                    "select s from StockItemEntity s where s.reservationId = :reservationId and s.deletedAt is null",
                    StockItemEntity.class)
                    .setParameter("reservationId", reservationId)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Map<Long, Integer> countByEditionId(List<Long> editionIds) {
        if (editionIds == null || editionIds.isEmpty()) {
            return Map.of();
        }
        try (Session session = sessionFactory.openSession()) {
            List<Object[]> results = session.createQuery(
                    "select s.editionId, count(s) from StockItemEntity s " +
                    "where s.editionId in :ids and s.deletedAt is null and s.enabled = true " +
                    "group by s.editionId",
                    Object[].class)
                    .setParameter("ids", editionIds)
                    .getResultList();
            Map<Long, Integer> counts = new HashMap<>();
            for (Object[] row : results) {
                counts.put(((Number) row[0]).longValue(), ((Number) row[1]).intValue());
            }
            return counts;
        }
    }

    @Override
    public List<StockItemEntity> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select s from StockItemEntity s where s.id in :ids and s.deletedAt is null order by s.code",
                    StockItemEntity.class)
                    .setParameter("ids", ids)
                    .getResultList();
        }
    }
}
