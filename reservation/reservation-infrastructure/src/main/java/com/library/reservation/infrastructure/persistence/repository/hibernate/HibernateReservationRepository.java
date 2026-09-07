package com.library.reservation.infrastructure.persistence.repository.hibernate;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.reservation.domain.model.ReservationStatus;
import com.library.reservation.infrastructure.persistence.entity.ReservationEntity;
import com.library.reservation.infrastructure.persistence.repository.jpa.ReservationJpaRepository;

public class HibernateReservationRepository extends AbstractHibernateRepository implements ReservationJpaRepository<ReservationEntity, Long> {

    public HibernateReservationRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<ReservationEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            ReservationEntity entity = session.createQuery(
                    "select r from ReservationEntity r where r.id = :id and r.deletedAt is null",
                    ReservationEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<ReservationEntity> findByIdIncludingDeleted(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(ReservationEntity.class, id));
        }
    }

    @Override
    public Optional<ReservationEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            ReservationEntity entity = session.createQuery(
                    "select r from ReservationEntity r where r.code = :code and r.deletedAt is null",
                    ReservationEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public List<ReservationEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select r from ReservationEntity r where r.deletedAt is null and r.enabled = true order by r.code",
                    ReservationEntity.class)
                    .getResultList();
        }
    }

    @Override
    public List<ReservationEntity> findAll(String status) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select r from ReservationEntity r where r.deletedAt is null";
            if ("active".equals(status)) {
                hql += " and r.status in ('DEPOSIT_PENDING', 'ACTIVE', 'OVERDUE')";
            } else if ("inactive".equals(status)) {
                hql += " and r.status in ('RETURNED', 'CANCELLED')";
            } else if (status != null && !status.isBlank() && !"all".equals(status)) {
                hql += " and r.status = :status";
            }
            hql += " order by r.createdAt desc";
            var query = session.createQuery(hql, ReservationEntity.class);
            if (status != null && !status.isBlank() && !"active".equals(status) && !"inactive".equals(status) && !"all".equals(status)) {
                query.setParameter("status", ReservationStatus.valueOf(status));
            }
            return query.getResultList();
        }
    }

    @Override
    public List<ReservationEntity> findByClientId(Long clientId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select r from ReservationEntity r where r.clientId = :clientId and r.deletedAt is null order by r.code",
                    ReservationEntity.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        }
    }

    @Override
    public List<ReservationEntity> findByEditionId(Long editionId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select r from ReservationEntity r where r.editionId = :editionId and r.deletedAt is null order by r.code",
                    ReservationEntity.class)
                    .setParameter("editionId", editionId)
                    .getResultList();
        }
    }

    @Override
    public List<ReservationEntity> findByStatus(ReservationStatus status) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select r from ReservationEntity r where r.status = :status and r.deletedAt is null order by r.code",
                    ReservationEntity.class)
                    .setParameter("status", status)
                    .getResultList();
        }
    }

    @Override
    public long countActiveByClientId(Long clientId) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                    "select count(r) from ReservationEntity r where r.clientId = :clientId and r.deletedAt is null and r.status in ('DEPOSIT_PENDING', 'ACTIVE', 'OVERDUE')",
                    Long.class)
                    .setParameter("clientId", clientId)
                    .uniqueResult();
            return count != null ? count : 0;
        }
    }

    @Override
    public ReservationEntity save(ReservationEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (ReservationEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        executeWithSession(session -> {
            ReservationEntity entity = session.get(ReservationEntity.class, id);
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
                "update ReservationEntity r set r.deletedAt = null, r.enabled = true where r.id = :id and r.deletedAt is not null")
                .setParameter("id", id)
                .executeUpdate());
    }
}
