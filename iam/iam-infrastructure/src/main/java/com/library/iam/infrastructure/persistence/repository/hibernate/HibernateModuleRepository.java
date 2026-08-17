package com.library.iam.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.iam.infrastructure.persistence.entity.ModuleEntity;
import com.library.iam.infrastructure.persistence.repository.jpa.ModuleJpaRepository;

/**
 * Hibernate-backed implementation of {@link ModuleJpaRepository}.
 *
 * <p>
 * All queries filter out logically-deleted modules
 * ({@code deletedAt IS NULL}).</p>
 */
public class HibernateModuleRepository implements ModuleJpaRepository<ModuleEntity, Long> {

    private final SessionFactory sessionFactory;

    public HibernateModuleRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<ModuleEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            ModuleEntity module = session.createQuery(
                    "select m from ModuleEntity m "
                    + "where m.code = :code and m.deletedAt is null "
                    + "and m.enabled = true",
                    ModuleEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(module);
        }
    }

    @Override
    public Optional<ModuleEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            ModuleEntity module = session.createQuery(
                    "select m from ModuleEntity m "
                    + "where m.id = :id and m.deletedAt is null and m.enabled = true",
                    ModuleEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(module);
        }
    }

    @Override
    public List<ModuleEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
"select m from ModuleEntity m "
                    + "where m.deletedAt is null and m.enabled = true "
                    + "order by m.sortOrder, m.code",
                    ModuleEntity.class)
                    .getResultList();
        }
    }

    @Override
    public ModuleEntity save(ModuleEntity module) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            ModuleEntity merged = (ModuleEntity) session.merge(module);
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
                    "update ModuleEntity m set m.deletedAt = :now, m.enabled = false "
                    + "where m.id = :id and m.deletedAt is null "
                    + "and m.enabled = true")
                    .setParameter("now", java.time.Instant.now())
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }
}
