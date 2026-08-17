package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.books.domain.model.Edition;
import com.library.books.infrastructure.persistence.entity.EditionEntity;
import com.library.books.infrastructure.persistence.mapper.EditionMapper;
import com.library.books.infrastructure.persistence.repository.jpa.EditionJpaRepository;

public class HibernateEditionRepository extends AbstractHibernateRepository implements EditionJpaRepository<EditionEntity, Long> {

    public HibernateEditionRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<EditionEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            EditionEntity edition = session.createQuery(
                    "select e from EditionEntity e where e.id = :id and e.deletedAt is null and e.enabled = true",
                    EditionEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(edition);
        }
    }

    @Override
    public List<EditionEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select e from EditionEntity e where e.deletedAt is null and e.enabled = true order by e.workId, e.editionNumber",
                    EditionEntity.class)
                    .getResultList();
        }
    }

    @Override
    public List<EditionEntity> findByWorkId(Long workId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select e from EditionEntity e where e.workId = :workId and e.deletedAt is null and e.enabled = true order by e.editionNumber",
                    EditionEntity.class)
                    .setParameter("workId", workId)
                    .getResultList();
        }
    }

    @Override
    public List<EditionEntity> findByPublisherId(Long publisherId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select e from EditionEntity e where e.publisherId = :publisherId and e.deletedAt is null and e.enabled = true order by e.workId, e.editionNumber",
                    EditionEntity.class)
                    .setParameter("publisherId", publisherId)
                    .getResultList();
        }
    }

    @Override
    public List<EditionEntity> findByFormatId(Long formatId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select e from EditionEntity e where e.formatId = :formatId and e.deletedAt is null and e.enabled = true order by e.workId, e.editionNumber",
                    EditionEntity.class)
                    .setParameter("formatId", formatId)
                    .getResultList();
        }
    }

    @Override
    public EditionEntity save(EditionEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (EditionEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        consumeWithSession(session -> session.createMutationQuery(
                "update EditionEntity e set e.deletedAt = :now, e.enabled = false where e.id = :id and e.deletedAt is null and e.enabled = true")
                .setParameter("now", java.time.Instant.now())
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public List<Edition> findSummariesByWorkId(Long workId) {
        try (Session session = sessionFactory.openSession()) {
            List<EditionEntity> editionEntities = session.createQuery(
                    "select e from EditionEntity e where e.workId = :workId and e.deletedAt is null and e.enabled = true order by e.editionNumber",
                    EditionEntity.class)
                    .setParameter("workId", workId)
                    .getResultList();
            return editionEntities.stream()
                    .map(EditionMapper::toDomain)
                    .toList();
        }
    }

    @Override
    public List<Edition> findSummariesByPublisherId(Long publisherId) {
        try (Session session = sessionFactory.openSession()) {
            List<EditionEntity> editionEntities = session.createQuery(
                    "select e from EditionEntity e where e.publisherId = :publisherId and e.deletedAt is null and e.enabled = true order by e.workId, e.editionNumber",
                    EditionEntity.class)
                    .setParameter("publisherId", publisherId)
                    .getResultList();
            return editionEntities.stream()
                    .map(EditionMapper::toDomain)
                    .toList();
        }
    }

    @Override
    public List<Edition> findSummariesByFormatId(Long formatId) {
        try (Session session = sessionFactory.openSession()) {
            List<EditionEntity> editionEntities = session.createQuery(
                    "select e from EditionEntity e where e.formatId = :formatId and e.deletedAt is null and e.enabled = true order by e.workId, e.editionNumber",
                    EditionEntity.class)
                    .setParameter("formatId", formatId)
                    .getResultList();
            return editionEntities.stream()
                    .map(EditionMapper::toDomain)
                    .toList();
        }
    }

    @Override
    public Optional<Edition> findDetailById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            EditionEntity editionEntity = session.createQuery(
                    "select e from EditionEntity e where e.id = :id and e.deletedAt is null and e.enabled = true",
                    EditionEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (editionEntity == null) {
                return Optional.empty();
            }
            return Optional.of(EditionMapper.toDomain(editionEntity));
        }
    }
}
