package com.library.security.port.in;

import com.library.security.model.SecurityAuditEvent;

/**
 * Primary port for security audit logging.
 * 
 * <p>This interface defines the contract for recording security events
 * for audit and compliance purposes. The domain layer defines WHAT security
 * events need to be audited, while the application layer provides the
 * implementation.</p>
 * 
 * <p>This follows the hexagonal architecture pattern where the domain layer
 * defines the interfaces (ports) that it needs from the outside world.</p>
 */
public interface SecurityAuditPort {
    
    /**
     * Records a security audit event.
     * 
     * @param event the security event to audit
     */
    void audit(SecurityAuditEvent event);
    
    /**
     * Records a security audit event with details.
     * 
     * @param eventType the type of security event
     * @param userId the user ID (may be null for anonymous events)
     * @param ipAddress the IP address of the source
     * @param details additional details about the event
     */
    void audit(String eventType, String userId, String ipAddress, String details);
}
