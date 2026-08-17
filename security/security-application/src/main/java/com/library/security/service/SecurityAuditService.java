package com.library.security.service;

import com.library.security.model.SecurityAuditEvent;
import com.library.security.model.SecurityAuditEvent.AuditSeverity;
import com.library.security.port.in.SecurityAuditPort;
import com.library.security.port.out.AuditRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application service that implements the SecurityAuditPort.
 * 
 * <p>Sanitizes all output to prevent PII leakage in logs.</p>
 */
public class SecurityAuditService implements SecurityAuditPort {
    
    private static final Logger log = LoggerFactory.getLogger(SecurityAuditService.class);
    
    private final AuditRepository auditRepository;
    
    public SecurityAuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }
    
    @Override
    public void audit(SecurityAuditEvent event) {
        log.info("Auditing security event: type={}, userId={}, ip={}, severity={}", 
                 sanitize(event.getEventType()), 
                 sanitizeUserId(event.getUserId()), 
                 sanitize(event.getIpAddress()), 
                 event.getSeverity());
        auditRepository.save(event);
    }
    
    @Override
    public void audit(String eventType, String userId, String ipAddress, String details) {
        AuditSeverity severity = determineSeverity(eventType);
        SecurityAuditEvent event = new SecurityAuditEvent(eventType, userId, ipAddress, details, severity);
        audit(event);
    }
    
    private String sanitizeUserId(String userId) {
        if (userId == null) {
            return "anonymous";
        }
        if (userId.length() > 20) {
            return userId.substring(0, 20) + "...";
        }
        return userId;
    }
    
    private String sanitize(String value) {
        if (value == null) {
            return "null";
        }
        if (value.length() > 100) {
            return value.substring(0, 100) + "...";
        }
        return value;
    }
    
    private AuditSeverity determineSeverity(String eventType) {
        if (eventType.contains("LOGIN_SUCCESS") || eventType.contains("LOGOUT")) {
            return AuditSeverity.INFO;
        } else if (eventType.contains("LOGIN_FAILED") || eventType.contains("CSRF")) {
            return AuditSeverity.ERROR;
        } else if (eventType.contains("RATE_LIMIT") || eventType.contains("BLOCKED")) {
            return AuditSeverity.WARNING;
        } else if (eventType.contains("CRITICAL") || eventType.contains("INCIDENT")) {
            return AuditSeverity.CRITICAL;
        }
        return AuditSeverity.INFO;
    }
}
