package com.library.stock.infrastructure.persistence.repository.hibernate;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.stock.infrastructure.persistence.entity.StockLocationEntity;
import com.library.stock.infrastructure.persistence.repository.jpa.StockLocationJpaRepository;

public class HibernateStockLocationRepository extends AbstractHibernateRepository implements StockLocationJpaRepository<StockLocationEntity, Long> {

    public HibernateStockLocationRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<StockLocationEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            StockLocationEntity entity = session.createQuery(
                    "select l from StockLocationEntity l where l.id = :id and l.deletedAt is null",
                    StockLocationEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<StockLocationEntity> findByIdIncludingDeleted(Long id) {
        try (Session session = sessionFactory.openSession()) {
            StockLocationEntity entity = session.createQuery(
                    "select l from StockLocationEntity l where l.id = :id",
                    StockLocationEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<StockLocationEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            StockLocationEntity entity = session.createQuery(
                    "select l from StockLocationEntity l where l.code = :code and l.deletedAt is null",
                    StockLocationEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public List<StockLocationEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select l from StockLocationEntity l where l.deletedAt is null and l.enabled = true order by l.code",
                    StockLocationEntity.class)
                    .getResultList();
        }
    }

    @Override
    public List<StockLocationEntity> findAll(String status) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select l from StockLocationEntity l";
            if ("active".equals(status)) {
                hql += " where l.deletedAt is null and l.enabled = true";
            } else if ("inactive".equals(status)) {
                hql += " where l.deletedAt is not null or l.enabled = false";
            }
            hql += " order by l.code";
            return session.createQuery(hql, StockLocationEntity.class).getResultList();
        }
    }

    @Override
    public StockLocationEntity save(StockLocationEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (StockLocationEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        executeWithSession(session -> {
            StockLocationEntity entity = session.get(StockLocationEntity.class, id);
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
                "update StockLocationEntity l set l.deletedAt = null, l.enabled = true where l.id = :id and l.deletedAt is not null")
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        try (Session session = sessionFactory.openSession()) {
            List<StockLocationEntity> results = session.createQuery(
                    "select l from StockLocationEntity l where l.id in :ids and l.deletedAt is null",
                    StockLocationEntity.class)
                    .setParameter("ids", ids)
                    .getResultList();
            return results.stream()
                    .collect(java.util.stream.Collectors.toMap(
                            StockLocationEntity::getId, StockLocationEntity::getName));
        }
    }

    @Override
    public long countStockItemsByLocationId(Long locationId) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                    "select count(s) from StockItemEntity s where s.locationId = :locationId and s.deletedAt is null",
                    Long.class)
                    .setParameter("locationId", locationId)
                    .uniqueResult();
            return count != null ? count : 0;
        }
    }
}
