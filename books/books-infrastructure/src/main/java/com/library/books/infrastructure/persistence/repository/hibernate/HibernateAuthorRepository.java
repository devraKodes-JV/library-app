package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.books.domain.model.Author;
import com.library.books.infrastructure.persistence.entity.AuthorEntity;
import com.library.books.infrastructure.persistence.entity.CategoryEntity;
import com.library.books.infrastructure.persistence.entity.LanguageEntity;
import com.library.books.infrastructure.persistence.entity.WorkAuthorEntity;
import com.library.books.infrastructure.persistence.entity.WorkEntity;
import com.library.books.infrastructure.persistence.mapper.AuthorMapper;
import com.library.books.infrastructure.persistence.repository.jpa.AuthorJpaRepository;
import com.library.books.domain.dto.common.WorkSummaryDTO;
import com.library.books.domain.dto.query.AuthorWithWorksDTO;

public class HibernateAuthorRepository extends AbstractHibernateRepository implements AuthorJpaRepository<AuthorEntity, Long> {

    public HibernateAuthorRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<AuthorEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            AuthorEntity author = session.createQuery(
                    "select a from AuthorEntity a where a.code = :code and a.deletedAt is null",
                    AuthorEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(author);
        }
    }

    @Override
    public Optional<AuthorEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            AuthorEntity author = session.createQuery(
                    "select a from AuthorEntity a where a.id = :id and a.deletedAt is null",
                    AuthorEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(author);
        }
    }

    @Override
    public List<AuthorEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select a from AuthorEntity a where a.deletedAt is null order by a.lastName, a.firstName",
                    AuthorEntity.class)
                    .getResultList();
        }
    }
    @Override
    public List<AuthorEntity> findAll(String status) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select a from AuthorEntity a";
            if ("active".equals(status)) hql += " where a.deletedAt is null";
            else if ("inactive".equals(status)) hql += " where a.deletedAt is not null";
            hql += " order by a.lastName, a.firstName";
            return session.createQuery(hql, AuthorEntity.class).getResultList();
        }
    }


    @Override
    public AuthorEntity save(AuthorEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (AuthorEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        executeWithSession(session -> {
            AuthorEntity entity = session.get(AuthorEntity.class, id);
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
            List<AuthorEntity> results = session.createQuery(
                    "select a from AuthorEntity a where a.id in :ids and a.deletedAt is null",
                    AuthorEntity.class)
                    .setParameter("ids", ids)
                    .getResultList();
            return results.stream()
                    .collect(java.util.stream.Collectors.toMap(AuthorEntity::getId, e -> e.getFirstName() + " " + e.getLastName()));
        }
    }

    @Override
    public Map<Long, String> findFullNamesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Map.of();
        }
        try (Session session = sessionFactory.openSession()) {
            List<AuthorEntity> results = session.createQuery(
                    "select a from AuthorEntity a where a.id in :ids and a.deletedAt is null",
                    AuthorEntity.class)
                    .setParameter("ids", ids)
                    .getResultList();
            return results.stream()
                    .collect(java.util.stream.Collectors.toMap(AuthorEntity::getId, e -> {
                        String fullName = (e.getFirstName() != null ? e.getFirstName() : "")
                                + (e.getFirstName() != null && !e.getFirstName().isBlank()
                                        && e.getLastName() != null && !e.getLastName().isBlank() ? " " : "")
                                + (e.getLastName() != null ? e.getLastName() : "");
                        return fullName.trim();
                    }));
        }
    }

    @Override
    public Optional<Author> findDetailById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            AuthorEntity authorEntity = session.createQuery(
                    "select a from AuthorEntity a where a.id = :id and a.deletedAt is null",
                    AuthorEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (authorEntity == null) {
                return Optional.empty();
            }

            return Optional.of(AuthorMapper.toDomain(authorEntity));
        }
    }

    public AuthorWithWorksDTO findByIdWithWorks(Long id) {
        try (Session session = sessionFactory.openSession()) {
            AuthorEntity authorEntity = session.createQuery(
                    "select distinct a from AuthorEntity a " +
                    "left join fetch a.workAuthors wa " +
                    "left join fetch wa.work w " +
                    "where a.id = :id and a.deletedAt is null",
                    AuthorEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (authorEntity == null) {
                return null;
            }

            List<WorkSummaryDTO> relatedWorks = authorEntity.getWorkAuthors() != null ? authorEntity.getWorkAuthors().stream()
                    .map(wa -> {
                        WorkEntity work = wa.getWork();
                        if (work == null) {
                            return null;
                        }
                        String languageName = null;
                        if (work.getOriginalLanguageId() != null) {
                            LanguageEntity language = session.createQuery(
                                    "select l from LanguageEntity l where l.id = :id and l.deletedAt is null",
                                    LanguageEntity.class)
                                    .setParameter("id", work.getOriginalLanguageId())
                                    .uniqueResult();
                            if (language != null) {
                                languageName = language.getName();
                            }
                        }
                        String categoryName = null;
                        if (work.getCategoryId() != null) {
                            CategoryEntity category = session.createQuery(
                                    "select c from CategoryEntity c where c.id = :id and c.deletedAt is null",
                                    CategoryEntity.class)
                                    .setParameter("id", work.getCategoryId())
                                    .uniqueResult();
                            if (category != null) {
                                categoryName = category.getName();
                            }
                        }
                        return new WorkSummaryDTO(work.getId(), work.getTitle(), work.getSubtitle(), languageName, categoryName);
                    })
                    .filter(java.util.Objects::nonNull)
                    .toList()
                    : List.of();

            return new AuthorWithWorksDTO(
                    authorEntity.getId(),
                    authorEntity.getFirstName(),
                    authorEntity.getLastName(),
                    authorEntity.getBiography(),
                    authorEntity.getBirthDate(),
                    authorEntity.getDeathDate(),
                    authorEntity.getCreatedAt(),
                    authorEntity.getUpdatedAt(),
                    relatedWorks
            );
        }
    }

    @Override
    public void softDeleteWorkAuthorsByAuthorId(Long authorId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update WorkAuthorEntity w set w.deletedAt = :now, w.enabled = false where w.authorId = :authorId and w.deletedAt is null and w.enabled = true")
                .setParameter("now", java.time.Instant.now())
                .setParameter("authorId", authorId)
                .executeUpdate());
    }

    @Override
    public void softDeleteEditionAuthorsByAuthorId(Long authorId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update EditionAuthorEntity e set e.deletedAt = :now, e.enabled = false where e.authorId = :authorId and e.deletedAt is null and e.enabled = true")
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
    public void softDeleteWorksByIds(List<Long> workIds) {
        if (workIds == null || workIds.isEmpty()) {
            return;
        }
        consumeWithSession(session -> session.createMutationQuery(
                        "update WorkEntity w set w.deletedAt = :now, w.enabled = false where w.id in :ids and w.deletedAt is null and w.enabled = true")
                .setParameter("now", java.time.Instant.now())
                .setParameter("ids", workIds)
                .executeUpdate());
    }

    @Override
    public void softDeleteEditionsByWorkIds(List<Long> workIds) {
        if (workIds == null || workIds.isEmpty()) {
            return;
        }
        consumeWithSession(session -> session.createMutationQuery(
                        "update EditionEntity e set e.deletedAt = :now, e.enabled = false where e.workId in :ids and e.deletedAt is null and e.enabled = true")
                .setParameter("now", java.time.Instant.now())
                .setParameter("ids", workIds)
                .executeUpdate());
    }

    @Override
    public void reactivateById(Long id) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update AuthorEntity a set a.deletedAt = null, a.enabled = true where a.id = :id and a.deletedAt is not null")
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public void reactivateWorksByAuthorId(Long authorId) {
        List<Long> workIds = findWorkIdsByAuthorId(authorId);
        for (Long workId : workIds) {
            Long activeAuthors = sessionFactory.openSession().createQuery(
                            "select count(wa) from WorkAuthorEntity wa where wa.workId = :workId and wa.deletedAt is null and wa.enabled = true",
                            Long.class)
                    .setParameter("workId", workId)
                    .uniqueResult();
            if (activeAuthors != null && activeAuthors > 0) {
                consumeWithSession(session -> session.createMutationQuery(
                                "update WorkEntity w set w.deletedAt = null, w.enabled = true where w.id = :id and w.deletedAt is not null")
                        .setParameter("id", workId)
                        .executeUpdate());
            }
        }
    }
}
