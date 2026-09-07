package com.library.accounting.infrastructure.persistence.repository.hibernate;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.accounting.domain.model.Refund;
import com.library.accounting.infrastructure.persistence.entity.RefundEntity;

public class HibernateRefundRepository {

    private final SessionFactory sessionFactory;

    public HibernateRefundRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Refund save(Refund refund) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            RefundEntity entity = RefundEntity.fromDomain(refund);
            if (entity.getId() == null) {
                session.persist(entity);
            } else {
                entity = session.merge(entity);
            }
            session.getTransaction().commit();
            return entity.toDomain();
        }
    }

    public Optional<Refund> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            RefundEntity e = session.get(RefundEntity.class, id);
            return Optional.ofNullable(e).map(RefundEntity::toDomain);
        }
    }

    public List<Refund> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from RefundEntity where deletedAt is null order by refundDate desc, id desc", RefundEntity.class)
                    .getResultList().stream().map(RefundEntity::toDomain).toList();
        }
    }
}
