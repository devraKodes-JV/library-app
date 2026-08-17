package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.books.infrastructure.persistence.entity.WorkAuthorEntity;
import com.library.books.infrastructure.persistence.repository.jpa.WorkAuthorJpaRepository;

public class HibernateWorkAuthorRepository extends AbstractHibernateRepository implements WorkAuthorJpaRepository<WorkAuthorEntity, Long> {

    public HibernateWorkAuthorRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<WorkAuthorEntity> findByWorkId(Long workId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkAuthorEntity w where w.workId = :workId and w.deletedAt is null and w.enabled = true",
                    WorkAuthorEntity.class)
                    .setParameter("workId", workId)
                    .getResultList();
        }
    }

    @Override
    public List<WorkAuthorEntity> findByAuthorId(Long authorId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkAuthorEntity w where w.authorId = :authorId and w.deletedAt is null and w.enabled = true",
                    WorkAuthorEntity.class)
                    .setParameter("authorId", authorId)
                    .getResultList();
        }
    }

    @Override
    public java.util.Optional<WorkAuthorEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            WorkAuthorEntity entity = session.createQuery(
                    "select w from WorkAuthorEntity w where w.id = :id and w.deletedAt is null and w.enabled = true",
                    WorkAuthorEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return java.util.Optional.ofNullable(entity);
        }
    }

    @Override
    public java.util.List<WorkAuthorEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkAuthorEntity w where w.deletedAt is null and w.enabled = true",
                    WorkAuthorEntity.class)
                    .getResultList();
        }
    }

    @Override
    public void deleteByWorkId(Long workId) {
        consumeWithSession(session -> session.createMutationQuery(
                "delete WorkAuthorEntity w where w.workId = :workId")
                .setParameter("workId", workId)
                .executeUpdate());
    }

    @Override
    public void deleteByAuthorId(Long authorId) {
        consumeWithSession(session -> session.createMutationQuery(
                "delete WorkAuthorEntity w where w.authorId = :authorId")
                .setParameter("authorId", authorId)
                .executeUpdate());
    }

    @Override
    public WorkAuthorEntity save(WorkAuthorEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (WorkAuthorEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        consumeWithSession(session -> session.createMutationQuery(
                "delete WorkAuthorEntity w where w.id = :id")
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public java.util.List<WorkAuthorEntity> findByWorkIds(java.util.List<Long> workIds) {
        if (workIds == null || workIds.isEmpty()) {
            return java.util.List.of();
        }
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkAuthorEntity w where w.workId in :workIds and w.deletedAt is null and w.enabled = true",
                    WorkAuthorEntity.class)
                    .setParameter("workIds", workIds)
                    .getResultList();
        }
    }
}
