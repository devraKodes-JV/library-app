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
}
