package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.books.infrastructure.persistence.entity.EditionAuthorEntity;
import com.library.books.infrastructure.persistence.repository.jpa.EditionAuthorJpaRepository;

public class HibernateEditionAuthorRepository extends AbstractHibernateRepository implements EditionAuthorJpaRepository<EditionAuthorEntity, Long> {

    public HibernateEditionAuthorRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<EditionAuthorEntity> findByEditionId(Long editionId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select e from EditionAuthorEntity e where e.editionId = :editionId and e.deletedAt is null",
                    EditionAuthorEntity.class)
                    .setParameter("editionId", editionId)
                    .getResultList();
        }
    }

    @Override
    public List<EditionAuthorEntity> findByAuthorId(Long authorId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select e from EditionAuthorEntity e where e.authorId = :authorId and e.deletedAt is null",
                    EditionAuthorEntity.class)
                    .setParameter("authorId", authorId)
                    .getResultList();
        }
    }

    @Override
    public java.util.Optional<EditionAuthorEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            EditionAuthorEntity entity = session.createQuery(
                    "select e from EditionAuthorEntity e where e.id = :id and e.deletedAt is null",
                    EditionAuthorEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return java.util.Optional.ofNullable(entity);
        }
    }

    @Override
    public java.util.List<EditionAuthorEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select e from EditionAuthorEntity e where e.deletedAt is null",
                    EditionAuthorEntity.class)
                    .getResultList();
        }
    }

    @Override
    public void deleteByEditionId(Long editionId) {
        executeWithSession(session -> {
            List<EditionAuthorEntity> entities = session.createQuery(
                            "select e from EditionAuthorEntity e where e.editionId = :editionId and e.deletedAt is null",
                            EditionAuthorEntity.class)
                    .setParameter("editionId", editionId)
                    .getResultList();
            for (EditionAuthorEntity entity : entities) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
    }

    @Override
    public void deleteByAuthorId(Long authorId) {
        executeWithSession(session -> {
            List<EditionAuthorEntity> entities = session.createQuery(
                            "select e from EditionAuthorEntity e where e.authorId = :authorId and e.deletedAt is null",
                            EditionAuthorEntity.class)
                    .setParameter("authorId", authorId)
                    .getResultList();
            for (EditionAuthorEntity entity : entities) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
    }

    @Override
    public EditionAuthorEntity save(EditionAuthorEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (EditionAuthorEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        executeWithSession(session -> {
            EditionAuthorEntity entity = session.get(EditionAuthorEntity.class, id);
            if (entity != null) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
    }

    @Override
    public void saveEditionAuthor(Long editionId, Long authorId, Long authorRoleId) {
        executeWithSession(session -> {
            EditionAuthorEntity entity = new EditionAuthorEntity();
            entity.setEditionId(editionId);
            entity.setAuthorId(authorId);
            entity.setAuthorRoleId(authorRoleId);
            entity.setEnabled(true);
            session.persist(entity);
            return null;
        });
    }
}
