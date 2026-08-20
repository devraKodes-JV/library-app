package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;

import com.library.books.domain.model.Edition;
import com.library.books.domain.dto.response.edition.EditionWithNamesDTO;
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
                    "select e from EditionEntity e where e.id = :id and e.deletedAt is null",
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
                    "select e from EditionEntity e where e.deletedAt is null order by e.workId, e.editionNumber",
                    EditionEntity.class)
                    .getResultList();
        }
    }

    @Override
    public List<EditionEntity> findByWorkId(Long workId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select e from EditionEntity e where e.workId = :workId and e.deletedAt is null order by e.editionNumber",
                    EditionEntity.class)
                    .setParameter("workId", workId)
                    .getResultList();
        }
    }

    @Override
    public List<EditionEntity> findByPublisherId(Long publisherId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select e from EditionEntity e where e.publisherId = :publisherId and e.deletedAt is null order by e.workId, e.editionNumber",
                    EditionEntity.class)
                    .setParameter("publisherId", publisherId)
                    .getResultList();
        }
    }

    @Override
    public List<EditionEntity> findByFormatId(Long formatId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select e from EditionEntity e where e.formatId = :formatId and e.deletedAt is null order by e.workId, e.editionNumber",
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
        executeWithSession(session -> {
            EditionEntity entity = session.get(EditionEntity.class, id);
            if (entity != null) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
    }

    @Override
    public List<Edition> findSummariesByWorkId(Long workId) {
        try (Session session = sessionFactory.openSession()) {
            List<EditionEntity> editionEntities = session.createQuery(
                    "select e from EditionEntity e where e.workId = :workId and e.deletedAt is null order by e.editionNumber",
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
                    "select e from EditionEntity e where e.publisherId = :publisherId and e.deletedAt is null order by e.workId, e.editionNumber",
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
                    "select e from EditionEntity e where e.formatId = :formatId and e.deletedAt is null order by e.workId, e.editionNumber",
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
                    "select e from EditionEntity e where e.id = :id and e.deletedAt is null",
                    EditionEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (editionEntity == null) {
                return Optional.empty();
            }
            return Optional.of(EditionMapper.toDomain(editionEntity));
        }
    }

    @Override
    public List<EditionWithNamesDTO> findByWorkIdWithDetails(Long workId) {
        try (Session session = sessionFactory.openSession()) {
            String sql = """
                    select e.id, e.work_id, w.title, e.publisher_id, p.name, e.format_id, f.name, e.language_id, l.name,
                           e.isbn, e.pages, e.publication_year, e.edition_number
                    from editions e
                    left join works w on e.work_id = w.id and w.deleted_at is null
                    left join publishers p on e.publisher_id = p.id and p.deleted_at is null
                    left join book_formats f on e.format_id = f.id and f.deleted_at is null
                    left join languages l on e.language_id = l.id and l.deleted_at is null
                    where e.work_id = :workId and e.deleted_at is null
                    order by e.edition_number
                    """;

            NativeQuery<Object[]> query = session.createNativeQuery(sql);
            query.setParameter("workId", workId);

            List<Object[]> rows = query.getResultList();
            return rows.stream()
                    .map(row -> new EditionWithNamesDTO(
                            ((Number) row[0]).longValue(),
                            ((Number) row[1]).longValue(),
                            (String) row[2],
                            ((Number) row[3]).longValue(),
                            (String) row[4],
                            ((Number) row[5]).longValue(),
                            (String) row[6],
                            ((Number) row[7]).longValue(),
                            (String) row[8],
                            (String) row[9],
                            (Integer) row[10],
                            (Integer) row[11],
                            (String) row[12]))
                    .toList();
        }
    }

    @Override
    public long countActiveByWorkId(Long workId) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                            "select count(e) from EditionEntity e where e.workId = :workId and e.deletedAt is null",
                            Long.class)
                    .setParameter("workId", workId)
                    .uniqueResult();
            return count != null ? count : 0;
        }
    }

    @Override
    public long countActiveByPublisherId(Long publisherId) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                            "select count(e) from EditionEntity e where e.publisherId = :publisherId and e.deletedAt is null",
                            Long.class)
                    .setParameter("publisherId", publisherId)
                    .uniqueResult();
            return count != null ? count : 0;
        }
    }

    @Override
    public long countActiveByFormatId(Long formatId) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                            "select count(e) from EditionEntity e where e.formatId = :formatId and e.deletedAt is null",
                            Long.class)
                    .setParameter("formatId", formatId)
                    .uniqueResult();
            return count != null ? count : 0;
        }
    }

    @Override
    public long countActiveByLanguageId(Long languageId) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                            "select count(e) from EditionEntity e where e.languageId = :languageId and e.deletedAt is null",
                            Long.class)
                    .setParameter("languageId", languageId)
                    .uniqueResult();
            return count != null ? count : 0;
        }
    }
}
