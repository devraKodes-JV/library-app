package com.library.security.web;

import com.library.security.service.SecurityAuditService;

import io.javalin.config.JavalinConfig;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * OWASP 2025 A02: Security Headers Filter.
 * 
 * <p>Implements comprehensive security headers with conditional HSTS
 * (only over HTTPS) and COEP credentialless for better compatibility.</p>
 */
public final class SecurityHeadersFilter {
    
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final int NONCE_BYTES = 16;
    
    private SecurityHeadersFilter() {
    }
    
    public static void register(JavalinConfig config, SecurityAuditService auditService) {
        config.routes.before(ctx -> {
            String nonce = generateNonce();
            ctx.attribute("securityNonce", nonce);
        });
        
        config.routes.after(ctx -> {
            String path = ctx.path();
            if (isStaticResourcePath(path)) {
                return;
            }
            
            String nonce = ctx.attribute("securityNonce");
            
            ctx.header("X-Content-Type-Options", "nosniff");
            ctx.header("X-Frame-Options", "DENY");
            ctx.header("X-XSS-Protection", "1; mode=block");
            ctx.header("Referrer-Policy", "strict-origin-when-cross-origin");
            ctx.header("Permissions-Policy", 
                "geolocation=(), " +
                "microphone=(), " +
                "camera=(), " +
                "payment=(), " +
                "usb=(), " +
                "magnetometer=(), " +
                "gyroscope=(), " +
                "accelerometer=(), " +
                "display-capture=()");
            
            ctx.header("Content-Security-Policy", 
                "default-src 'none'; " +
                "script-src 'self' 'nonce-" + nonce + "'; " +
                "style-src 'self' 'nonce-" + nonce + "'; " +
                "img-src 'self' data:; " +
                "font-src 'self' data:; " +
                "connect-src 'self' " + getAllowedApiOrigins() + "; " +
                "frame-ancestors 'none'; " +
                "form-action 'self'; " +
                "base-uri 'self'; " +
                "upgrade-insecure-requests");
            
            if (isHttps(ctx)) {
                ctx.header("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
            }
            
            ctx.header("Cross-Origin-Opener-Policy", "same-origin");
            ctx.header("Cross-Origin-Resource-Policy", "same-origin");
            ctx.header("Cross-Origin-Embedder-Policy", "credentialless");
            ctx.header("X-Download-Options", "noopen");
            ctx.header("X-Permitted-Cross-Domain-Policies", "none");
        });
    }
    
    private static boolean isStaticResourcePath(String path) {
        return path.startsWith("/static/") 
            || path.startsWith("/css/") 
            || path.startsWith("/js/") 
            || path.startsWith("/vendor/")
            || path.equals("/favicon.ico");
    }
    
    private static String getAllowedApiOrigins() {
        String origins = System.getenv("ALLOWED_API_ORIGINS");
        if (origins == null || origins.isBlank()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String origin : origins.split(",")) {
            origin = origin.trim();
            if (!origin.isBlank()) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(origin);
            }
        }
        return sb.toString();
    }
    
    private static boolean isHttps(io.javalin.http.Context ctx) {
        return ctx.req().isSecure() || 
               "https".equalsIgnoreCase(ctx.header("X-Forwarded-Proto"));
    }
    
    private static String generateNonce() {
        byte[] nonceBytes = new byte[NONCE_BYTES];
        secureRandom.nextBytes(nonceBytes);
        return Base64.getEncoder().encodeToString(nonceBytes);
    }
}
