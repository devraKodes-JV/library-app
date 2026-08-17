package com.library.iam.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.iam.infrastructure.persistence.entity.RoleEntity;
import com.library.iam.infrastructure.persistence.repository.jpa.RoleJpaRepository;

/**
 * Hibernate-backed implementation of {@link RoleJpaRepository}.
 *
 * <p>
 * All queries filter out logically-deleted roles ({@code deletedAt IS NULL}),
 * so deleted roles never surface to the application layer.</p>
 */
public class HibernateRoleRepository implements RoleJpaRepository<RoleEntity, Long> {

    private final SessionFactory sessionFactory;

    public HibernateRoleRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<RoleEntity> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            RoleEntity role = session.createQuery(
                    "select distinct r from RoleEntity r "
                    + "left join fetch r.permissions p "
                    + "left join fetch p.module "
                    + "where r.name = :name and r.deletedAt is null "
                    + "and r.enabled = true",
                    RoleEntity.class)
                    .setParameter("name", name)
                    .uniqueResult();
            return Optional.ofNullable(role);
        }
    }

    @Override
    public Optional<RoleEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            RoleEntity role = session.createQuery(
                    "select distinct r from RoleEntity r "
                    + "left join fetch r.permissions p "
                    + "left join fetch p.module "
                    + "where r.id = :id and r.deletedAt is null "
                    + "and r.enabled = true",
                    RoleEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(role);
        }
    }

    @Override
    public List<RoleEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select distinct r from RoleEntity r "
                    + "left join fetch r.permissions p "
                    + "left join fetch p.module "
                    + "where r.deletedAt is null "
                    + "and r.enabled = true "
                    + "order by r.name",
                    RoleEntity.class)
                    .getResultList();
        }
    }

    @Override
    public RoleEntity save(RoleEntity role) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            RoleEntity merged = (RoleEntity) session.merge(role);
            session.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // Logical delete: mark the deleted_at timestamp instead of removing
            // the row, preserving history and referential integrity.
            session.createMutationQuery(
                    "update RoleEntity r set r.deletedAt = :now, r.enabled = false "
                    + "where r.id = :id and r.deletedAt is null and r.enabled = true")
                    .setParameter("now", java.time.Instant.now())
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }
}
