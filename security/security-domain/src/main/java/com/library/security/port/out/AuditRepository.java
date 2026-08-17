package com.library.security.port.out;

import com.library.security.model.SecurityAuditEvent;

/**
 * Secondary port for persisting security audit events.
 * 
 * <p>This interface defines the contract for storing security audit events.
 * The domain layer declares that it needs a way to persist audit events,
 * while the infrastructure layer provides the concrete implementation (e.g., JPA).</p>
 * 
 * <p>This follows the hexagonal architecture pattern where the domain layer
 * defines the interfaces (ports) that it needs from the outside world.</p>
 */
public interface AuditRepository {
    
    /**
     * Saves a security audit event.
     * 
     * @param event the security event to persist
     */
    void save(SecurityAuditEvent event);
    
    /**
     * Finds audit events by user ID.
     * 
     * @param userId the user ID to search for
     * @return iterable of audit events for the user
     */
    Iterable<SecurityAuditEvent> findByUserId(String userId);
    
    /**
     * Finds audit events by event type.
     * 
     * @param eventType the event type to search for
     * @return iterable of audit events of the specified type
     */
    Iterable<SecurityAuditEvent> findByEventType(String eventType);
}
