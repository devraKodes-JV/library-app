package com.library.security.persistence.entity;

import com.library.security.model.SecurityAuditEvent.AuditSeverity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import java.time.Instant;

@Entity
@Table(name = "security_audit_events")
public class SecurityAuditEventEntity {
    
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;
    
    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;
    
    @Column(name = "user_id", length = 50)
    private String userId;
    
    @Column(name = "ip_address", length = 45)
    private String ipAddress;
    
    @Column(name = "details", length = 500)
    private String details;
    
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
    
    @Column(name = "severity", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AuditSeverity severity;
    
    public SecurityAuditEventEntity() {
        // Default constructor for JPA
    }
    
    public SecurityAuditEventEntity(String id, String eventType, String userId, 
                                    String ipAddress, String details, Instant timestamp, 
                                    AuditSeverity severity) {
        this.id = id;
        this.eventType = eventType;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.details = details;
        this.timestamp = timestamp;
        this.severity = severity;
    }
    
    // Getters and setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getEventType() {
        return eventType;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
    
    public AuditSeverity getSeverity() {
        return severity;
    }
    
    public void setSeverity(AuditSeverity severity) {
        this.severity = severity;
    }
}
