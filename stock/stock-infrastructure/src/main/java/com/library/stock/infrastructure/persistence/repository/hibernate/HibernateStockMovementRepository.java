package com.library.stock.infrastructure.persistence.repository.hibernate;

import java.time.Instant;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.library.iam.infrastructure.security.CurrentUser;
import com.library.stock.infrastructure.persistence.entity.StockMovementEntity;
import com.library.stock.infrastructure.persistence.mapper.StockMovementMapper;
import com.library.stock.infrastructure.persistence.repository.jpa.StockMovementJpaRepository;

public class HibernateStockMovementRepository extends AbstractHibernateRepository implements StockMovementJpaRepository {

    public HibernateStockMovementRepository(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<StockMovementEntity> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select m from StockMovementEntity m order by m.createdAt desc",
                    StockMovementEntity.class)
                    .getResultList();
        }
    }

    @Override
    public List<StockMovementEntity> findByStockItemId(Long stockItemId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "select m from StockMovementEntity m where m.stockItemId = :stockItemId order by m.createdAt desc",
                    StockMovementEntity.class)
                    .setParameter("stockItemId", stockItemId)
                    .getResultList();
        }
    }

    @Override
    public StockMovementEntity save(StockMovementEntity movement) {
        return executeWithSession(session -> {
            movement.setCreatedAt(Instant.now());
            movement.setCreatedBy(CurrentUser.getOrDefault("system"));
            session.persist(movement);
            return movement;
        });
    }
}
