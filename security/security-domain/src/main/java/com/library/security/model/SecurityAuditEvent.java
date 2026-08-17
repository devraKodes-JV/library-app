package com.library.security.model;

import java.time.Instant;

/**
 * Domain entity representing a security audit event.
 * 
 * <p>This entity captures security-relevant events for audit trail,
 * compliance, and forensic analysis. Security events include login attempts,
 * CSRF violations, rate limiting, unauthorized access attempts, etc.</p>
 */
public class SecurityAuditEvent {
    
    private final String eventType;
    private final String userId;
    private final String ipAddress;
    private final String details;
    private final Instant timestamp;
    private final AuditSeverity severity;
    
    public SecurityAuditEvent(String eventType, String userId, String ipAddress, 
                              String details, AuditSeverity severity) {
        this(eventType, userId, ipAddress, details, Instant.now(), severity);
    }
    
    public SecurityAuditEvent(String eventType, String userId, String ipAddress,
                              String details, Instant timestamp, AuditSeverity severity) {
        this.eventType = eventType;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.details = details;
        this.timestamp = timestamp;
        this.severity = severity;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public String getDetails() {
        return details;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public AuditSeverity getSeverity() {
        return severity;
    }
    
    /**
     * Severity levels for security events.
     */
    public enum AuditSeverity {
        INFO,      // Normal security events (successful login, logout)
        WARNING,   // Potentially suspicious events (rate limit hit)
        ERROR,     // Security failures (login attempt, CSRF violation)
        CRITICAL   // Critical security incidents (multiple failed logins)
    }
}
