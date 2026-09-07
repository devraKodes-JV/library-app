package com.library.accounting.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.accounting.domain.model.payroll.PayrollPayment;
import com.library.accounting.infrastructure.persistence.entity.PayrollPaymentEntity;

public class HibernatePayrollRepository {

    private final SessionFactory sessionFactory;

    public HibernatePayrollRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public PayrollPayment save(PayrollPayment payment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            PayrollPaymentEntity entity = PayrollPaymentEntity.fromDomain(payment);
            if (entity.getId() == null) {
                session.persist(entity);
            } else {
                entity = session.merge(entity);
            }
            session.getTransaction().commit();
            return entity.toDomain();
        }
    }

    public Optional<PayrollPayment> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            PayrollPaymentEntity e = session.get(PayrollPaymentEntity.class, id);
            return Optional.ofNullable(e).map(PayrollPaymentEntity::toDomain);
        }
    }

    public List<PayrollPayment> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PayrollPaymentEntity where deletedAt is null order by paymentDate desc, id desc", PayrollPaymentEntity.class)
                    .getResultList().stream().map(PayrollPaymentEntity::toDomain).toList();
        }
    }

    public List<PayrollPayment> findByEmployeeId(Long employeeId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PayrollPaymentEntity where deletedAt is null and employeeId = :employeeId order by paymentDate desc", PayrollPaymentEntity.class)
                    .setParameter("employeeId", employeeId)
                    .getResultList().stream().map(PayrollPaymentEntity::toDomain).toList();
        }
    }

    public List<PayrollPayment> findByPeriod(Integer month, Integer year) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PayrollPaymentEntity where deletedAt is null and periodMonth = :month and periodYear = :year order by paymentDate desc", PayrollPaymentEntity.class)
                    .setParameter("month", month)
                    .setParameter("year", year)
                    .getResultList().stream().map(PayrollPaymentEntity::toDomain).toList();
        }
    }

    public List<PayrollPayment> findByDateRange(java.time.LocalDate from, java.time.LocalDate to) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PayrollPaymentEntity where deletedAt is null and paymentDate between :from and :to order by paymentDate desc", PayrollPaymentEntity.class)
                    .setParameter("from", from)
                    .setParameter("to", to)
                    .getResultList().stream().map(PayrollPaymentEntity::toDomain).toList();
        }
    }
}
