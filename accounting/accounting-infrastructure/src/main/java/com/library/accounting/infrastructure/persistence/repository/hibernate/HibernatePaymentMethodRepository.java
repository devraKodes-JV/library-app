package com.library.accounting.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.accounting.domain.model.PaymentMethod;
import com.library.accounting.infrastructure.persistence.entity.PaymentMethodEntity;
import com.library.accounting.infrastructure.persistence.mapper.PaymentMethodMapper;

public class HibernatePaymentMethodRepository {
    private final SessionFactory sessionFactory;

    public HibernatePaymentMethodRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<PaymentMethod> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PaymentMethodEntity order by code asc", PaymentMethodEntity.class)
                    .getResultList().stream().map(PaymentMethodMapper::toDomain).toList();
        }
    }

    public List<PaymentMethod> findAllEnabled() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from PaymentMethodEntity where enabled = true order by code asc", PaymentMethodEntity.class)
                    .getResultList().stream().map(PaymentMethodMapper::toDomain).toList();
        }
    }

    public Optional<PaymentMethod> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            PaymentMethodEntity e = session.get(PaymentMethodEntity.class, id);
            return Optional.ofNullable(e).map(PaymentMethodMapper::toDomain);
        }
    }

    public Optional<PaymentMethod> findByCode(String code) {
        try (Session session = sessionFactory.openSession()) {
            PaymentMethodEntity e = session.createQuery("from PaymentMethodEntity where code = :code", PaymentMethodEntity.class)
                    .setParameter("code", code)
                    .uniqueResult();
            return Optional.ofNullable(e).map(PaymentMethodMapper::toDomain);
        }
    }

    public PaymentMethod save(PaymentMethod paymentMethod) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            PaymentMethodEntity entity = PaymentMethodMapper.toEntity(paymentMethod);
            if (entity.getId() == null) {
                session.persist(entity);
            } else {
                entity = session.merge(entity);
            }
            session.getTransaction().commit();
            return PaymentMethodMapper.toDomain(entity);
        }
    }

    public void deleteById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            PaymentMethodEntity entity = session.get(PaymentMethodEntity.class, id);
            if (entity != null) {
                session.remove(entity);
            }
            session.getTransaction().commit();
        }
    }
}
