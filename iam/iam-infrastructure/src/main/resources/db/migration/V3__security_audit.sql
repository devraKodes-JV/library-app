-- Security audit events table for OWASP 2025 A09: Security Logging & Alerting Failures
-- This table stores security events for audit trail, compliance, and forensic analysis
CREATE TABLE security_audit_events (
    id VARCHAR(100) PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    user_id VARCHAR(50),
    ip_address VARCHAR(45),
    details VARCHAR(500),
    timestamp TIMESTAMP NOT NULL,
    severity VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for efficient querying (OWASP A09: Security Logging & Alerting)
CREATE INDEX idx_security_audit_user_id ON security_audit_events(user_id);
CREATE INDEX idx_security_audit_event_type ON security_audit_events(event_type);
CREATE INDEX idx_security_audit_timestamp ON security_audit_events(timestamp);
CREATE INDEX idx_security_audit_severity ON security_audit_events(severity);

-- Comment explaining the table purpose
COMMENT ON TABLE security_audit_events IS 'Security audit events for OWASP 2025 compliance - tracks security incidents like CSRF violations, rate limiting, login failures, etc.';
