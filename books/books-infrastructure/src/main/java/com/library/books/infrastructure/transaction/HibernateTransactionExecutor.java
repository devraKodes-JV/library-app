package com.library.books.infrastructure.transaction;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.kernel.transaction.Transactional;

public class HibernateTransactionExecutor implements Transactional {

    private static final Logger log = LoggerFactory.getLogger(HibernateTransactionExecutor.class);

    private static final ScopedValue<Session> CURRENT_SESSION = ScopedValue.newInstance();

    private final SessionFactory sessionFactory;

    public HibernateTransactionExecutor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public <T> T execute(Supplier<T> work) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                T result = ScopedValue.where(CURRENT_SESSION, session).call(() -> work.get());
                tx.commit();
                return result;
            } catch (Exception e) {
                tx.rollback();
                log.error("Transaction rolled back due to error", e);
                throw e;
            }
        }
    }

    public static Session currentSession() {
        try {
            return CURRENT_SESSION.get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
