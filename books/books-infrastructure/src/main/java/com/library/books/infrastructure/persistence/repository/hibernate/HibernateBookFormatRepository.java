package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.books.domain.model.BookFormat;
import com.library.books.domain.model.Edition;
import com.library.books.infrastructure.persistence.entity.BookFormatEntity;
import com.library.books.infrastructure.persistence.entity.EditionEntity;
import com.library.books.infrastructure.persistence.entity.EditionEntity;
import com.library.books.infrastructure.persistence.mapper.BookFormatMapper;
import com.library.books.infrastructure.persistence.mapper.EditionMapper;
import com.library.books.infrastructure.persistence.repository.jpa.BookFormatJpaRepository;

public class HibernateBookFormatRepository extends AbstractHibernateRepository implements BookFormatJpaRepository<BookFormatEntity, Long> {

    public HibernateBookFormatRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<BookFormatEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            BookFormatEntity format = session.createQuery(
                    "select f from BookFormatEntity f where f.code = :code and f.deletedAt is null",
                    BookFormatEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(format);
        }
    }

    @Override
    public Optional<BookFormatEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            BookFormatEntity format = session.createQuery(
                    "select f from BookFormatEntity f where f.id = :id and f.deletedAt is null",
                    BookFormatEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(format);
        }
    }

    @Override
    public List<BookFormatEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select f from BookFormatEntity f where f.deletedAt is null order by f.name",
                    BookFormatEntity.class)
                    .getResultList();
        }
    }
    @Override
    public List<BookFormatEntity> findAll(String status) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select b from BookFormatEntity b";
            if ("active".equals(status)) hql += " where b.deletedAt is null";
            else if ("inactive".equals(status)) hql += " where b.deletedAt is not null";
            hql += " order by b.name";
            return session.createQuery(hql, BookFormatEntity.class).getResultList();
        }
    }


    @Override
    public BookFormatEntity save(BookFormatEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (BookFormatEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        executeWithSession(session -> {
            BookFormatEntity entity = session.get(BookFormatEntity.class, id);
            if (entity != null) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        try (Session session = sessionFactory.openSession()) {
            List<BookFormatEntity> results = session.createQuery(
                    "select f from BookFormatEntity f where f.id in :ids and f.deletedAt is null",
                    BookFormatEntity.class)
                    .setParameter("ids", ids)
                    .getResultList();
            return results.stream()
                    .collect(java.util.stream.Collectors.toMap(BookFormatEntity::getId, BookFormatEntity::getName));
        }
    }

    @Override
    public Optional<BookFormat> findDetailById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            BookFormatEntity formatEntity = session.createQuery(
                    "select f from BookFormatEntity f where f.id = :id and f.deletedAt is null",
                    BookFormatEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (formatEntity == null) {
                return Optional.empty();
            }

            return Optional.of(BookFormatMapper.toDomain(formatEntity));
        }
    }

    @Override
    public List<Edition> findEditionsByFormatId(Long formatId) {
        try (Session session = sessionFactory.openSession()) {
            List<EditionEntity> editionEntities = session.createQuery(
                    "select e from EditionEntity e where e.formatId = :formatId and e.deletedAt is null order by e.editionNumber",
                    EditionEntity.class)
                    .setParameter("formatId", formatId)
                    .getResultList();
            return editionEntities.stream()
                    .map(EditionMapper::toDomain)
                    .toList();
        }
    }

    @Override
    public void softDeleteEditionsByFormatId(Long formatId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update EditionEntity e set e.deletedAt = :now, e.enabled = false where e.formatId = :formatId and e.deletedAt is null and e.enabled = true")
                .setParameter("now", java.time.Instant.now())
                .setParameter("formatId", formatId)
                .executeUpdate());
    }


    @Override
    public void reactivateById(Long id) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update BookFormatEntity b set b.deletedAt = null, b.enabled = true where b.id = :id and b.deletedAt is not null")
                .setParameter("id", id)
                .executeUpdate());
    }


    @Override
    public void reactivateEditionsByFormatId(Long formatId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update EditionEntity e set e.deletedAt = null, e.enabled = true where e.formatId = :formatId and e.deletedAt is not null")
                .setParameter("formatId", formatId)
                .executeUpdate());
    }

}
