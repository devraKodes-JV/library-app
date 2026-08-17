package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.books.domain.model.Edition;
import com.library.books.domain.model.Publisher;
import com.library.books.infrastructure.persistence.entity.PublisherEntity;
import com.library.books.infrastructure.persistence.mapper.EditionMapper;
import com.library.books.infrastructure.persistence.mapper.PublisherMapper;
import com.library.books.infrastructure.persistence.repository.jpa.PublisherJpaRepository;

public class HibernatePublisherRepository extends AbstractHibernateRepository implements PublisherJpaRepository<PublisherEntity, Long> {

    public HibernatePublisherRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<PublisherEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            PublisherEntity publisher = session.createQuery(
                    "select p from PublisherEntity p where p.code = :code and p.deletedAt is null and p.enabled = true",
                    PublisherEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(publisher);
        }
    }

    @Override
    public Optional<PublisherEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            PublisherEntity publisher = session.createQuery(
                    "select p from PublisherEntity p where p.id = :id and p.deletedAt is null and p.enabled = true",
                    PublisherEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(publisher);
        }
    }

    @Override
    public List<PublisherEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select p from PublisherEntity p where p.deletedAt is null and p.enabled = true order by p.name",
                    PublisherEntity.class)
                    .getResultList();
        }
    }

    @Override
    public PublisherEntity save(PublisherEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (PublisherEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        consumeWithSession(session -> session.createMutationQuery(
                "update PublisherEntity p set p.deletedAt = :now, p.enabled = false where p.id = :id and p.deletedAt is null and p.enabled = true")
                .setParameter("now", java.time.Instant.now())
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        try (Session session = sessionFactory.openSession()) {
            List<PublisherEntity> results = session.createQuery(
                    "select p from PublisherEntity p where p.id in :ids and p.deletedAt is null and p.enabled = true",
                    PublisherEntity.class)
                    .setParameter("ids", ids)
                    .getResultList();
            return results.stream()
                    .collect(java.util.stream.Collectors.toMap(PublisherEntity::getId, PublisherEntity::getName));
        }
    }

    @Override
    public Optional<Publisher> findDetailById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            PublisherEntity publisherEntity = session.createQuery(
                    "select p from PublisherEntity p where p.id = :id and p.deletedAt is null and p.enabled = true",
                    PublisherEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (publisherEntity == null) {
                return Optional.empty();
            }
            return Optional.of(PublisherMapper.toDomain(publisherEntity));
        }
    }

    @Override
    public List<Edition> findSummariesByPublisherId(Long publisherId) {
        try (Session session = sessionFactory.openSession()) {
            List<com.library.books.infrastructure.persistence.entity.EditionEntity> editionEntities = session.createQuery(
                    "select e from EditionEntity e where e.publisherId = :publisherId and e.deletedAt is null and e.enabled = true order by e.workId, e.editionNumber",
                    com.library.books.infrastructure.persistence.entity.EditionEntity.class)
                    .setParameter("publisherId", publisherId)
                    .getResultList();
            return editionEntities.stream()
                    .map(EditionMapper::toDomain)
                    .toList();
        }
    }
}
