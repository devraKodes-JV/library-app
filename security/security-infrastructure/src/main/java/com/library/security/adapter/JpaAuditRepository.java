package com.library.security.adapter;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.library.security.model.SecurityAuditEvent;
import com.library.security.port.out.AuditRepository;
import com.library.security.persistence.entity.SecurityAuditEventEntity;

public class JpaAuditRepository implements AuditRepository {
    
    private final SessionFactory sessionFactory;
    
    public JpaAuditRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public void save(SecurityAuditEvent event) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            SecurityAuditEventEntity entity = new SecurityAuditEventEntity(
                generateThreadSafeId(event),
                event.getEventType(),
                event.getUserId(),
                event.getIpAddress(),
                event.getDetails(),
                event.getTimestamp(),
                event.getSeverity()
            );
            session.persist(entity);
            session.getTransaction().commit();
        }
    }
    
    private String generateThreadSafeId(SecurityAuditEvent event) {
        return event.getEventType() + "_" + 
               event.getTimestamp().toEpochMilli() + "_" + 
               UUID.randomUUID().toString().substring(0, 8);
    }
    
    @Override
    public Iterable<SecurityAuditEvent> findByUserId(String userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<SecurityAuditEventEntity> query = session.createQuery(
                "from SecurityAuditEventEntity e where e.userId = :userId order by e.timestamp desc",
                SecurityAuditEventEntity.class
            ).setParameter("userId", userId);
            List<SecurityAuditEventEntity> entities = query.getResultList();
            return entities.stream().map(this::toDomain).toList();
        }
    }
    
    @Override
    public Iterable<SecurityAuditEvent> findByEventType(String eventType) {
        try (Session session = sessionFactory.openSession()) {
            Query<SecurityAuditEventEntity> query = session.createQuery(
                "from SecurityAuditEventEntity e where e.eventType = :eventType order by e.timestamp desc",
                SecurityAuditEventEntity.class
            ).setParameter("eventType", eventType);
            List<SecurityAuditEventEntity> entities = query.getResultList();
            return entities.stream().map(this::toDomain).toList();
        }
    }
    
    private SecurityAuditEvent toDomain(SecurityAuditEventEntity entity) {
        return new SecurityAuditEvent(
            entity.getEventType(),
            entity.getUserId(),
            entity.getIpAddress(),
            entity.getDetails(),
            entity.getTimestamp(),
            entity.getSeverity()
        );
    }
}
