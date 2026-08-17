package com.library.books.infrastructure.persistence.repository.hibernate;

import java.util.function.Consumer;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class AbstractHibernateRepository {

    protected final SessionFactory sessionFactory;

    protected AbstractHibernateRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected <T> T executeWithSession(Function<Session, T> operation) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            T result = operation.apply(session);
            session.getTransaction().commit();
            return result;
        }
    }

    protected void consumeWithSession(Consumer<Session> operation) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            operation.accept(session);
            session.getTransaction().commit();
        }
    }
}
