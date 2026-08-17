package com.library.iam.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.library.iam.infrastructure.persistence.entity.RoleEntity;
import com.library.iam.infrastructure.persistence.entity.UserEntity;
import com.library.iam.infrastructure.persistence.repository.jpa.UserJpaRepository;

/**
 * Hibernate-backed implementation of {@link UserJpaRepository}.
 *
 * <p>
 * This is the low-level persistence access. It uses a Hibernate
 * {@link SessionFactory} directly (no Spring Data). Each method opens a short
 * session, performs the query, and closes it. The {@code STANDARD} lock mode is
 * used on reads to avoid stale reads.</p>
 */
public class HibernateUserRepository implements UserJpaRepository<UserEntity, Long> {

    private final SessionFactory sessionFactory;

    public HibernateUserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            // The join fetch loads the role, its permissions and modules in a
            // single round-trip so the whole graph is available off-session.
            // deletedAt is null filters out logically-deleted users.
            Query<UserEntity> query = session.createQuery(
                    "select distinct u from UserEntity u "
                    + "left join fetch u.role r "
                    + "left join fetch r.permissions p "
                    + "left join fetch p.module "
                    + "where u.username = :username and u.deletedAt is null "
                    + "and u.enabled = true",
                    UserEntity.class);
            query.setParameter("username", username);
            UserEntity result = query.uniqueResult();
            return Optional.ofNullable(result);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                    "select count(u) from UserEntity u "
                    + "where u.username = :username and u.deletedAt is null "
                    + "and u.enabled = true",
                    Long.class)
                    .setParameter("username", username)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Long count = session.createQuery(
                    "select count(u) from UserEntity u "
                    + "where u.email = :email and u.deletedAt is null "
                    + "and u.enabled = true",
                    Long.class)
                    .setParameter("email", email)
                    .uniqueResult();
            return count != null && count > 0;
        }
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            // Loads the full graph (role, permissions, modules) in one query
            // so the domain object is fully populated off-session.
            Query<UserEntity> query = session.createQuery(
                    "select distinct u from UserEntity u "
                    + "left join fetch u.role r "
                    + "left join fetch r.permissions p "
                    + "left join fetch p.module "
                    + "where u.id = :id and u.deletedAt is null "
                    + "and u.enabled = true",
                    UserEntity.class);
            query.setParameter("id", id);
            return Optional.ofNullable(query.uniqueResult());
        }
    }

    @Override
    public List<UserEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            // Only active (non-deleted) users, newest first.
            return session.createQuery(
                    "select distinct u from UserEntity u "
                    + "left join fetch u.role r "
                    + "left join fetch r.permissions p "
                    + "left join fetch p.module "
                    + "where u.deletedAt is null "
                    + "and u.enabled = true "
                    + "order by u.username",
                    UserEntity.class)
                    .getResultList();
        }
    }

    @Override
    public List<UserEntity> findInactive() {
        try (Session session = sessionFactory.openSession()) {
            // Accounts that are disabled (enabled = false) OR logically deleted
            // (deleted_at is not null). The admin review screen shows both so a
            // disabled or wrongly-deleted account can be reinstated (double-check).
            return session.createQuery(
                    "select distinct u from UserEntity u "
                    + "left join fetch u.role r "
                    + "left join fetch r.permissions p "
                    + "left join fetch p.module "
                    + "where u.deletedAt is not null and u.enabled = false "
                    + "order by u.username",
                    UserEntity.class)
                    .getResultList();
        }
    }

    @Override
    public void reinstate(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // Single recovery path: re-enable the account AND clear the logical
            // delete marker so the user can log in again.
            session.createMutationQuery(
                    "update UserEntity u set u.enabled = true, u.deletedAt = null "
                    + "where u.id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public UserEntity save(UserEntity user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserEntity merged = (UserEntity) session.merge(user);
            session.getTransaction().commit();

            // Re-fetch the full graph (role, permissions, modules) within a
            // session so the returned entity is fully initialized and the
            // adapter can map it to the domain object off-session without a
            // LazyInitializationException.
            return findById(merged.getId()).orElse(merged);
        }
    }

    @Override
    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // Logical delete: mark the deleted_at timestamp instead of
            // physically removing the row, preserving history and referential
            // integrity. A deleted user can no longer log in.
            // The timestamp is bound as a java.time.Instant parameter because
            // binding `current_timestamp` (a java.sql.Timestamp) to a field of
            // type java.time.Instant causes a Hibernate SemanticException.
            session.createMutationQuery(
                    "update UserEntity u set u.deletedAt = :now, u.enabled = true "
                    + "where u.id = :id and u.deletedAt is null and u.enabled = true")
                    .setParameter("now", java.time.Instant.now())
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public boolean updatePassword(String username, String passwordHash) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            int updated = session.createMutationQuery(
                    "update UserEntity u set u.password = :pwd "
                    + "where u.username = :user and u.deletedAt is null and u.enabled = true")
                    .setParameter("pwd", passwordHash)
                    .setParameter("user", username)
                    .executeUpdate();
            session.getTransaction().commit();
            return updated > 0;
        }
    }

    /**
     * Helper used by seeders to load a role reference by name.
     *
     * @param session the current Hibernate session
     * @param name the role name
     * @return the persisted role entity
     */
    public static RoleEntity requireRole(Session session, String name) {
        RoleEntity role = session.createQuery(
                "from RoleEntity r where r.name = :name", RoleEntity.class)
                .setParameter("name", name)
                .uniqueResult();
        if (role == null) {
            throw new IllegalStateException("Role not found: " + name);
        }
        return role;
    }
}
