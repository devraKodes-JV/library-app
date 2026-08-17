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
                    "select a from AuthorEntity a where a.code = :code and a.deletedAt is null and a.enabled = true",
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
                    "select a from AuthorEntity a where a.id = :id and a.deletedAt is null and a.enabled = true",
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
                    "select a from AuthorEntity a where a.deletedAt is null and a.enabled = true order by a.lastName, a.firstName",
                    AuthorEntity.class)
                    .getResultList();
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
        consumeWithSession(session -> session.createMutationQuery(
                "update AuthorEntity a set a.deletedAt = :now, a.enabled = false where a.id = :id and a.deletedAt is null and a.enabled = true")
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
            List<AuthorEntity> results = session.createQuery(
                    "select a from AuthorEntity a where a.id in :ids and a.deletedAt is null and a.enabled = true",
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
                    "select a from AuthorEntity a where a.id in :ids and a.deletedAt is null and a.enabled = true",
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
                    "select a from AuthorEntity a where a.id = :id and a.deletedAt is null and a.enabled = true",
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
                    "where a.id = :id and a.deletedAt is null and a.enabled = true",
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
                                    "select l from LanguageEntity l where l.id = :id and l.deletedAt is null and l.enabled = true",
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
                                    "select c from CategoryEntity c where c.id = :id and c.deletedAt is null and c.enabled = true",
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
}
