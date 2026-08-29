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
                    "select w from WorkAuthorEntity w where w.workId = :workId and w.deletedAt is null",
                    WorkAuthorEntity.class)
                    .setParameter("workId", workId)
                    .getResultList();
        }
    }

    @Override
    public List<WorkAuthorEntity> findByAuthorId(Long authorId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkAuthorEntity w where w.authorId = :authorId and w.deletedAt is null",
                    WorkAuthorEntity.class)
                    .setParameter("authorId", authorId)
                    .getResultList();
        }
    }

    @Override
    public java.util.Optional<WorkAuthorEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            WorkAuthorEntity entity = session.createQuery(
                    "select w from WorkAuthorEntity w where w.id = :id and w.deletedAt is null",
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
                    "select w from WorkAuthorEntity w where w.deletedAt is null",
                    WorkAuthorEntity.class)
                    .getResultList();
        }
    }

    @Override
    public void deleteByWorkId(Long workId) {
        executeWithSession(session -> {
            List<WorkAuthorEntity> entities = session.createQuery(
                            "select w from WorkAuthorEntity w where w.workId = :workId and w.deletedAt is null",
                            WorkAuthorEntity.class)
                    .setParameter("workId", workId)
                    .getResultList();
            for (WorkAuthorEntity entity : entities) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
    }

    @Override
    public void deleteByAuthorId(Long authorId) {
        executeWithSession(session -> {
            List<WorkAuthorEntity> entities = session.createQuery(
                            "select w from WorkAuthorEntity w where w.authorId = :authorId and w.deletedAt is null",
                            WorkAuthorEntity.class)
                    .setParameter("authorId", authorId)
                    .getResultList();
            for (WorkAuthorEntity entity : entities) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
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
        executeWithSession(session -> {
            WorkAuthorEntity entity = session.get(WorkAuthorEntity.class, id);
            if (entity != null) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
    }

    @Override
    public java.util.List<WorkAuthorEntity> findByWorkIds(java.util.List<Long> workIds) {
        if (workIds == null || workIds.isEmpty()) {
            return java.util.List.of();
        }
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkAuthorEntity w where w.workId in :workIds and w.deletedAt is null",
                    WorkAuthorEntity.class)
                    .setParameter("workIds", workIds)
                    .getResultList();
        }
    }

    @Override
    public void saveWorkAuthor(Long workId, Long authorId, Long authorRoleId) {
        executeWithSession(session -> {
            WorkAuthorEntity entity = new WorkAuthorEntity();
            entity.setWorkId(workId);
            entity.setAuthorId(authorId);
            entity.setAuthorRoleId(authorRoleId);
            entity.setEnabled(true);
            session.persist(entity);
            return null;
        });
    }

    @Override
    public void softDeleteByAuthorId(Long authorId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update WorkAuthorEntity w set w.deletedAt = :now, w.enabled = false where w.authorId = :authorId and w.deletedAt is null and w.enabled = true")
                .setParameter("now", java.time.Instant.now())
                .setParameter("authorId", authorId)
                .executeUpdate());
    }


    @Override
    public List<Long> findWorkIdsByAuthorId(Long authorId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "select distinct wa.workId from WorkAuthorEntity wa where wa.authorId = :authorId and wa.deletedAt is null",
                            Long.class)
                    .setParameter("authorId", authorId)
                    .getResultList();
        }
    }


    @Override
    public long countActiveAuthorsByWorkId(Long workId) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                            "select count(wa) from WorkAuthorEntity wa where wa.workId = :workId and wa.deletedAt is null and wa.enabled = true",
                            Long.class)
                    .setParameter("workId", workId)
                    .uniqueResult();
            return count != null ? count : 0;
        }
    }


    @Override
    public void reactivateByAuthorId(Long authorId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update WorkAuthorEntity w set w.deletedAt = null, w.enabled = true where w.authorId = :authorId and w.deletedAt is not null")
                .setParameter("authorId", authorId)
                .executeUpdate());
    }


    @Override
    public void reactivateByWorkId(Long workId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update WorkAuthorEntity w set w.deletedAt = null, w.enabled = true where w.workId = :workId and w.deletedAt is not null")
                .setParameter("workId", workId)
                .executeUpdate());
    }

}
