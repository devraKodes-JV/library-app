package com.library.iam.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.iam.infrastructure.persistence.entity.PermissionEntity;
import com.library.iam.infrastructure.persistence.repository.jpa.PermissionJpaRepository;

/**
 * Hibernate-backed implementation of {@link PermissionJpaRepository}.
 *
 * <p>
 * All queries filter out logically-deleted permissions
 * ({@code deletedAt IS NULL}).</p>
 */
public class HibernatePermissionRepository implements PermissionJpaRepository<PermissionEntity, Long> {

    private final SessionFactory sessionFactory;

    public HibernatePermissionRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<PermissionEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            PermissionEntity permission = session.createQuery(
                    "select distinct p from PermissionEntity p "
                    + "left join fetch p.module "
                    + "where p.code = :code and p.deletedAt is null "
                    + "and p.enabled = true",
                    PermissionEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(permission);
        }
    }

    @Override
    public Optional<PermissionEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            PermissionEntity permission = session.createQuery(
                    "select distinct p from PermissionEntity p "
                    + "left join fetch p.module "
                    + "where p.id = :id and p.deletedAt is null "
                    + "and p.enabled = true",
                    PermissionEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(permission);
        }
    }

    @Override
    public List<PermissionEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select distinct p from PermissionEntity p "
                    + "left join fetch p.module "
                    + "where p.deletedAt is null "
                    + "and p.enabled = true "
                    + "order by p.module.id, p.sortOrder",
                    PermissionEntity.class)
                    .getResultList();
        }
    }

    @Override
    public PermissionEntity save(PermissionEntity permission) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            PermissionEntity merged = (PermissionEntity) session.merge(permission);
            session.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // Logical delete: mark the deleted_at timestamp instead of
            // physically removing the row, preserving history and referential
            // integrity.
            session.createMutationQuery(
                    "update PermissionEntity p set p.deletedAt = :now, p.enabled = false "
                    + "where p.id = :id and p.deletedAt is null "
                    + "and p.enabled = true")
                    .setParameter("now", java.time.Instant.now())
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }
}
