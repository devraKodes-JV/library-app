package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.books.infrastructure.persistence.entity.AuthorRoleEntity;
import com.library.books.infrastructure.persistence.repository.jpa.AuthorRoleJpaRepository;

public class HibernateAuthorRoleRepository extends AbstractHibernateRepository implements AuthorRoleJpaRepository<AuthorRoleEntity, Long> {

    public HibernateAuthorRoleRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<AuthorRoleEntity> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(AuthorRoleEntity.class, id));
        }
    }

    @Override
    public Optional<AuthorRoleEntity> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            AuthorRoleEntity entity = session.createQuery(
                    "select r from AuthorRoleEntity r where r.code = :code",
                    AuthorRoleEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public Optional<AuthorRoleEntity> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            AuthorRoleEntity entity = session.createQuery(
                    "select r from AuthorRoleEntity r where r.name = :name",
                    AuthorRoleEntity.class)
                    .setParameter("name", name)
                    .setMaxResults(1)
                    .uniqueResult();
            return Optional.ofNullable(entity);
        }
    }

    @Override
    public List<AuthorRoleEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createNativeQuery(
                    "select * from author_roles where deleted_at is null",
                    AuthorRoleEntity.class)
                    .getResultList();
        }
    }
    @Override
    public List<AuthorRoleEntity> findAll(String status) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "select r from AuthorRoleEntity r";
            if ("active".equals(status)) hql += " where r.deletedAt is null";
            else if ("inactive".equals(status)) hql += " where r.deletedAt is not null";
            hql += " order by r.name";
            return session.createQuery(hql, AuthorRoleEntity.class).getResultList();
        }
    }


    @Override
    public AuthorRoleEntity save(AuthorRoleEntity entity) {
        return executeWithSession(session -> {
            if (entity.getId() == null) {
                session.persist(entity);
                return entity;
            }
            return (AuthorRoleEntity) session.merge(entity);
        });
    }

    @Override
    public void deleteById(Long id) {
        executeWithSession(session -> {
            AuthorRoleEntity entity = session.get(AuthorRoleEntity.class, id);
            if (entity != null) {
                entity.markDeleted();
                session.merge(entity);
            }
            return null;
        });
    }

    @Override
    public void softDeleteWorkAuthorsByRoleId(Long roleId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update WorkAuthorEntity w set w.deletedAt = :now, w.enabled = false where w.authorRoleId = :roleId and w.deletedAt is null and w.enabled = true")
                .setParameter("now", java.time.Instant.now())
                .setParameter("roleId", roleId)
                .executeUpdate());
    }


    @Override
    public void softDeleteEditionAuthorsByRoleId(Long roleId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update EditionAuthorEntity e set e.deletedAt = :now, e.enabled = false where e.authorRoleId = :roleId and e.deletedAt is null and e.enabled = true")
                .setParameter("now", java.time.Instant.now())
                .setParameter("roleId", roleId)
                .executeUpdate());
    }


    @Override
    public void reactivateById(Long id) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update AuthorRoleEntity r set r.deletedAt = null, r.enabled = true where r.id = :id and r.deletedAt is not null")
                .setParameter("id", id)
                .executeUpdate());
    }


    @Override
    public void reactivateWorkAuthorsByRoleId(Long roleId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update WorkAuthorEntity w set w.deletedAt = null, w.enabled = true where w.authorRoleId = :roleId and w.deletedAt is not null")
                .setParameter("roleId", roleId)
                .executeUpdate());
    }


    @Override
    public void reactivateEditionAuthorsByRoleId(Long roleId) {
        consumeWithSession(session -> session.createMutationQuery(
                        "update EditionAuthorEntity e set e.deletedAt = null, e.enabled = true where e.authorRoleId = :roleId and e.deletedAt is not null")
                .setParameter("roleId", roleId)
                .executeUpdate());
    }

}
