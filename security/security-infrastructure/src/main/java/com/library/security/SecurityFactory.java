package com.library.security;

import org.hibernate.SessionFactory;

import com.library.security.adapter.JpaAuditRepository;
import com.library.security.port.out.AuditRepository;
import com.library.security.service.SecurityAuditService;
import com.library.security.web.SecurityHeadersFilter;
import com.library.security.web.CsrfProtectionFilter;
import com.library.security.web.RateLimitingFilter;

import io.javalin.config.JavalinConfig;

public final class SecurityFactory {
    
    private SecurityFactory() {
    }
    
    public static SecurityAuditService register(JavalinConfig config, SessionFactory sessionFactory) {
        AuditRepository auditRepository = new JpaAuditRepository(sessionFactory);
        SecurityAuditService auditService = new SecurityAuditService(auditRepository);
        
        SecurityHeadersFilter.register(config, auditService);
        CsrfProtectionFilter.register(config, auditService);
        RateLimitingFilter.register(config, auditService);
        
        return auditService;
    }
}
