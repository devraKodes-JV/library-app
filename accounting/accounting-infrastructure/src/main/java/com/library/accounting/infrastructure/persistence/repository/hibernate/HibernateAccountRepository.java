package com.library.accounting.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.accounting.domain.model.Account;
import com.library.accounting.infrastructure.persistence.entity.AccountEntity;

public class HibernateAccountRepository {

    private final SessionFactory sessionFactory;

    public HibernateAccountRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Account save(Account account) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            AccountEntity entity = AccountEntity.fromDomain(account);
            if (entity.getId() == null) {
                session.persist(entity);
            } else {
                entity = session.merge(entity);
            }
            session.getTransaction().commit();
            return entity.toDomain();
        }
    }

    public Optional<Account> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            AccountEntity e = session.get(AccountEntity.class, id);
            return Optional.ofNullable(e).map(AccountEntity::toDomain);
        }
    }

    public Optional<Account> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from AccountEntity where code = :code", AccountEntity.class)
                    .setParameter("code", code)
                    .getResultStream().findFirst().map(AccountEntity::toDomain);
        }
    }

    public List<Account> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from AccountEntity where deletedAt is null order by code", AccountEntity.class)
                    .getResultList().stream().map(AccountEntity::toDomain).toList();
        }
    }

    public void delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            AccountEntity e = session.get(AccountEntity.class, id);
            if (e != null) {
                e.markDeleted();
                session.merge(e);
            }
            session.getTransaction().commit();
        }
    }
}
