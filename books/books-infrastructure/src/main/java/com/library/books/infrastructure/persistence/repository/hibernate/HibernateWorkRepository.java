package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.books.domain.model.Work;
import com.library.books.infrastructure.transaction.HibernateTransactionExecutor;
import com.library.books.infrastructure.persistence.entity.AuthorEntity;
import com.library.books.infrastructure.persistence.entity.CategoryEntity;
import com.library.books.infrastructure.persistence.entity.LanguageEntity;
import com.library.books.infrastructure.persistence.entity.WorkAuthorEntity;
import com.library.books.infrastructure.persistence.entity.WorkEntity;
import com.library.books.infrastructure.persistence.mapper.WorkMapper;
import com.library.books.infrastructure.persistence.repository.jpa.WorkJpaRepository;
import com.library.books.domain.dto.common.FlatAuthorDTO;
import com.library.books.domain.dto.query.WorkWithRelationsDTO;

public class HibernateWorkRepository extends AbstractHibernateRepository implements WorkJpaRepository<WorkEntity, Long> {

    public HibernateWorkRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected <T> T executeWithSession(java.util.function.Function<Session, T> operation) {
        Session session = HibernateTransactionExecutor.currentSession();
        if (session != null) {
            return operation.apply(session);
        }
        return super.executeWithSession(operation);
    }

    @Override
    protected void consumeWithSession(java.util.function.Consumer<Session> operation) {
        Session session = HibernateTransactionExecutor.currentSession();
        if (session != null) {
            operation.accept(session);
            return;
        }
        super.consumeWithSession(operation);
    }

    @Override
    public Optional<WorkEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            WorkEntity work = session.createQuery(
                    "select w from WorkEntity w where w.code = :code and w.deletedAt is null and w.enabled = true",
                    WorkEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(work);
        }
    }

    @Override
    public Optional<WorkEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            WorkEntity work = session.createQuery(
                    "select w from WorkEntity w where w.id = :id and w.deletedAt is null and w.enabled = true",
                    WorkEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(work);
        }
    }

    @Override
    public List<WorkEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkEntity w where w.deletedAt is null and w.enabled = true order by w.title",
                    WorkEntity.class)
                    .getResultList();
        }
    }

    @Override
    public List<WorkEntity> findByCategoryId(Long categoryId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkEntity w where w.categoryId = :categoryId and w.deletedAt is null and w.enabled = true order by w.title",
                    WorkEntity.class)
                    .setParameter("categoryId", categoryId)
                    .getResultList();
        }
    }

    @Override
    public List<WorkEntity> findByOriginalLanguageId(Long languageId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkEntity w where w.originalLanguageId = :languageId and w.deletedAt is null and w.enabled = true order by w.title",
                    WorkEntity.class)
                    .setParameter("languageId", languageId)
                    .getResultList();
        }
    }

    @Override
    public List<WorkEntity> findByAuthorId(Long authorId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkEntity w " +
                    "join WorkAuthorEntity wa on w.id = wa.workId " +
                    "where wa.authorId = :authorId and w.deletedAt is null and w.enabled = true " +
                    "order by w.title",
                    WorkEntity.class)
                    .setParameter("authorId", authorId)
                    .getResultList();
        }
    }

    @Override
    public WorkEntity save(WorkEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (WorkEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        consumeWithSession(session -> session.createMutationQuery(
                "update WorkEntity w set w.deletedAt = :now, w.enabled = false where w.id = :id and w.deletedAt is null and w.enabled = true")
                .setParameter("now", java.time.Instant.now())
                .setParameter("id", id)
                .executeUpdate());
    }

    @Override
    public List<WorkEntity> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select w from WorkEntity w where w.id in :ids and w.deletedAt is null and w.enabled = true",
                    WorkEntity.class)
                    .setParameter("ids", ids)
                    .getResultList();
        }
    }

    @Override
    public boolean existsLanguage(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                    "select count(l) from LanguageEntity l where l.id = :id and l.deletedAt is null and l.enabled = true",
                    Long.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public boolean existsCategory(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                    "select count(c) from CategoryEntity c where c.id = :id and c.deletedAt is null and c.enabled = true",
                    Long.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public void saveWorkAuthor(Long workId, Long authorId) {
        consumeWithSession(session -> session.createMutationQuery(
                "insert into WorkAuthorEntity (workId, authorId, role, enabled, createdAt, updatedAt) "
                + "values (:workId, :authorId, 'Author', true, :now, :now)")
                .setParameter("workId", workId)
                .setParameter("authorId", authorId)
                .setParameter("now", java.time.Instant.now())
                .executeUpdate());
    }

    @Override
    public void deleteWorkAuthorsByWorkId(Long workId) {
        consumeWithSession(session -> session.createMutationQuery(
                "delete WorkAuthorEntity w where w.workId = :workId")
                .setParameter("workId", workId)
                .executeUpdate());
    }

    public WorkWithRelationsDTO findByIdWithRelations(Long id) {
        try (Session session = sessionFactory.openSession()) {
            WorkEntity workEntity = session.createQuery(
                    "select w from WorkEntity w " +
                    "left join fetch w.workAuthors wa " +
                    "left join fetch wa.author a " +
                    "where w.id = :id and w.deletedAt is null and w.enabled = true",
                    WorkEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (workEntity == null) {
                return null;
            }

            String languageName = null;
            if (workEntity.getOriginalLanguageId() != null) {
                LanguageEntity language = session.createQuery(
                        "select l from LanguageEntity l where l.id = :id and l.deletedAt is null and l.enabled = true",
                        LanguageEntity.class)
                        .setParameter("id", workEntity.getOriginalLanguageId())
                        .uniqueResult();
                if (language != null) {
                    languageName = language.getName();
                }
            }

            String categoryName = null;
            if (workEntity.getCategoryId() != null) {
                CategoryEntity category = session.createQuery(
                        "select c from CategoryEntity c where c.id = :id and c.deletedAt is null and c.enabled = true",
                        CategoryEntity.class)
                        .setParameter("id", workEntity.getCategoryId())
                        .uniqueResult();
                if (category != null) {
                    categoryName = category.getName();
                }
            }

            List<FlatAuthorDTO> authorDTOs = workEntity.getWorkAuthors() != null ? workEntity.getWorkAuthors().stream()
                    .map(wa -> {
                        AuthorEntity author = wa.getAuthor();
                        String fullName = author != null ? (author.getFirstName() + " " + author.getLastName()).trim() : "";
                        return new FlatAuthorDTO(wa.getAuthorId(), fullName, wa.getRole());
                    })
                    .toList()
                    : List.of();

            return new WorkWithRelationsDTO(
                    workEntity.getId(),
                    workEntity.getTitle(),
                    workEntity.getSubtitle(),
                    workEntity.getSummary(),
                    workEntity.getCreatedAt(),
                    workEntity.getUpdatedAt(),
                    workEntity.getOriginalLanguageId(),
                    languageName != null ? languageName : "",
                    workEntity.getCategoryId(),
                    categoryName != null ? categoryName : "",
                    authorDTOs
            );
        }
    }
}
