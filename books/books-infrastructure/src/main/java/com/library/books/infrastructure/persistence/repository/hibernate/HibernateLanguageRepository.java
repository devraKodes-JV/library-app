package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.books.domain.model.Language;
import com.library.books.domain.model.Work;
import com.library.books.infrastructure.persistence.entity.LanguageEntity;
import com.library.books.infrastructure.persistence.mapper.LanguageMapper;
import com.library.books.infrastructure.persistence.mapper.WorkMapper;
import com.library.books.infrastructure.persistence.repository.jpa.LanguageJpaRepository;

public class HibernateLanguageRepository extends AbstractHibernateRepository implements LanguageJpaRepository<LanguageEntity, Long> {

    public HibernateLanguageRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<LanguageEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            LanguageEntity language = session.createQuery(
                    "select l from LanguageEntity l where l.code = :code and l.deletedAt is null and l.enabled = true",
                    LanguageEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(language);
        }
    }

    @Override
    public Optional<LanguageEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            LanguageEntity language = session.createQuery(
                    "select l from LanguageEntity l where l.id = :id and l.deletedAt is null and l.enabled = true",
                    LanguageEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(language);
        }
    }

    @Override
    public List<LanguageEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select l from LanguageEntity l where l.deletedAt is null and l.enabled = true order by l.name",
                    LanguageEntity.class)
                    .getResultList();
        }
    }

    @Override
    public LanguageEntity save(LanguageEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (LanguageEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        consumeWithSession(session -> session.createMutationQuery(
                "update LanguageEntity l set l.deletedAt = :now, l.enabled = false where l.id = :id and l.deletedAt is null and l.enabled = true")
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
            List<LanguageEntity> results = session.createQuery(
                    "select l from LanguageEntity l where l.id in :ids and l.deletedAt is null and l.enabled = true",
                    LanguageEntity.class)
                    .setParameter("ids", ids)
                    .getResultList();
            return results.stream()
                    .collect(java.util.stream.Collectors.toMap(LanguageEntity::getId, LanguageEntity::getName));
        }
    }

    @Override
    public Optional<Language> findDetailById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            LanguageEntity languageEntity = session.createQuery(
                    "select l from LanguageEntity l where l.id = :id and l.deletedAt is null and l.enabled = true",
                    LanguageEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            if (languageEntity == null) {
                return Optional.empty();
            }
            return Optional.of(LanguageMapper.toDomain(languageEntity));
        }
    }

    @Override
    public List<Work> findRelatedWorks(Long languageId) {
        try (Session session = sessionFactory.openSession()) {
            List<com.library.books.infrastructure.persistence.entity.WorkEntity> workEntities = session.createQuery(
                    "select w from WorkEntity w where w.originalLanguageId = :languageId and w.deletedAt is null and w.enabled = true order by w.title",
                    com.library.books.infrastructure.persistence.entity.WorkEntity.class)
                    .setParameter("languageId", languageId)
                    .getResultList();
            return workEntities.stream()
                    .map(WorkMapper::toDomain)
                    .toList();
        }
    }
}
