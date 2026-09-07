package com.library.accounting.infrastructure.persistence.repository.hibernate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.accounting.domain.model.Payment;
import com.library.accounting.infrastructure.persistence.entity.PaymentEntity;

public class HibernatePaymentRepository {

    private final SessionFactory sessionFactory;

    public HibernatePaymentRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Payment save(Payment payment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            PaymentEntity entity = PaymentEntity.fromDomain(payment);
            if (entity.getId() == null) {
                session.persist(entity);
            } else {
                entity = session.merge(entity);
            }
            session.getTransaction().commit();
            return entity.toDomain();
        }
    }

    public Optional<Payment> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            PaymentEntity e = session.get(PaymentEntity.class, id);
            return Optional.ofNullable(e).map(PaymentEntity::toDomain);
        }
    }

    public List<Payment> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PaymentEntity where deletedAt is null order by paymentDate desc, id desc", PaymentEntity.class)
                    .getResultList().stream().map(PaymentEntity::toDomain).toList();
        }
    }

    public List<Payment> findByDateRange(LocalDate from, LocalDate to) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PaymentEntity where deletedAt is null and paymentDate between :from and :to order by paymentDate desc", PaymentEntity.class)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList().stream().map(PaymentEntity::toDomain).toList();
        }
    }

    public List<Payment> findByClientId(Long clientId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PaymentEntity where deletedAt is null and clientId = :clientId order by paymentDate desc", PaymentEntity.class)
                    .setParameter("clientId", clientId)
                    .getResultList().stream().map(PaymentEntity::toDomain).toList();
        }
    }
}
