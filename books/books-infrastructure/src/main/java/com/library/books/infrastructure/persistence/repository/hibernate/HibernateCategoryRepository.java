package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.books.domain.model.Category;
import com.library.books.domain.model.Work;
import com.library.books.infrastructure.persistence.entity.CategoryEntity;
import com.library.books.infrastructure.persistence.mapper.CategoryMapper;
import com.library.books.infrastructure.persistence.mapper.WorkMapper;
import com.library.books.infrastructure.persistence.repository.jpa.CategoryJpaRepository;

public class HibernateCategoryRepository extends AbstractHibernateRepository implements CategoryJpaRepository<CategoryEntity, Long> {

    public HibernateCategoryRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<CategoryEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            CategoryEntity category = session.createQuery(
                    "select c from CategoryEntity c where c.code = :code and c.deletedAt is null",
                    CategoryEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(category);
        }
    }

    @Override
    public Optional<CategoryEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            CategoryEntity category = session.createQuery(
                    "select c from CategoryEntity c where c.id = :id and c.deletedAt is null",
                    CategoryEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(category);
        }
    }

    @Override
    public List<CategoryEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select c from CategoryEntity c where c.deletedAt is null order by c.name",
                    CategoryEntity.class)
                    .getResultList();
        }
    }

    @Override
    public CategoryEntity save(CategoryEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (CategoryEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        executeWithSession(session -> {
            CategoryEntity entity = session.get(CategoryEntity.class, id);
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
            List<CategoryEntity> results = session.createQuery(
                    "select c from CategoryEntity c where c.id in :ids and c.deletedAt is null",
                    CategoryEntity.class)
                    .setParameter("ids", ids)
                    .getResultList();
            return results.stream()
                    .collect(java.util.stream.Collectors.toMap(CategoryEntity::getId, CategoryEntity::getName));
        }
    }

    @Override
    public Optional<Category> findDetailById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            CategoryEntity categoryEntity = session.createQuery(
                    "select c from CategoryEntity c where c.id = :id and c.deletedAt is null",
                    CategoryEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();

            if (categoryEntity == null) {
                return Optional.empty();
            }

            return Optional.of(CategoryMapper.toDomain(categoryEntity));
        }
    }

    @Override
    public List<Work> findRelatedWorks(Long categoryId) {
        try (Session session = sessionFactory.openSession()) {
            List<com.library.books.infrastructure.persistence.entity.WorkEntity> workEntities = session.createQuery(
                    "select w from WorkEntity w where w.categoryId = :categoryId and w.deletedAt is null order by w.title",
                    com.library.books.infrastructure.persistence.entity.WorkEntity.class)
                    .setParameter("categoryId", categoryId)
                    .getResultList();
            return workEntities.stream()
                    .map(WorkMapper::toDomain)
                    .toList();
        }
    }

    public void nullifyParent(Long parentId) {
        consumeWithSession(session -> session.createMutationQuery(
                "update CategoryEntity c set c.parentId = null where c.parentId = :parentId and c.deletedAt is null")
                .setParameter("parentId", parentId)
                .executeUpdate());
    }
}
