package com.library.security.web;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.library.security.service.SecurityAuditService;

import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.Cookie;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.SameSite;
import jakarta.servlet.http.HttpSession;

public final class CsrfProtectionFilter {
    
    private static final String CSRF_COOKIE_NAME = "__Host-XSRF-TOKEN";
    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    private static final String CSRF_FORM_FIELD_NAME = "_csrf";
    private static final String CSRF_ATTR_NAME = "com.library.security.csrfToken";
    private static final int TOKEN_BYTES = 32;
    private static final int HMAC_KEY_BYTES = 32;
    private static final long TOKEN_EXPIRATION_SECONDS = 3600;
    
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final byte[] HMAC_SECRET = loadHmacSecret();
    
    private CsrfProtectionFilter() {}
    
    private static byte[] loadHmacSecret() {
        String envSecret = System.getenv("CSRF_HMAC_SECRET");
        if (envSecret != null && !envSecret.isEmpty()) {
            try {
                return Base64.getDecoder().decode(envSecret);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid CSRF_HMAC_SECRET in env. Using random (NOT recommended for production).");
            }
        }
        byte[] secret = new byte[HMAC_KEY_BYTES];
        secureRandom.nextBytes(secret);
        System.err.println("WARNING: Temporary HMAC_SECRET generated. Set CSRF_HMAC_SECRET in production.");
        return secret;
    }
    
    public static void register(JavalinConfig config, SecurityAuditService auditService) {
        config.routes.before(ctx -> {
            if (!isStateChangingRequest(ctx.req().getMethod()) && shouldEnsureToken(ctx.path())) {
                ensureCsrfToken(ctx);
            }
        });
        
        config.routes.before(ctx -> {
            if (isStateChangingRequest(ctx.req().getMethod()) && shouldValidateCsrf(ctx.path())) {
                try {
                    validateCsrfToken(ctx);
                } catch (ForbiddenResponse e) {
                    auditService.audit(
                        "CSRF_VIOLATION",
                        null,
                        ctx.ip(),
                        "CSRF validation failed: " + ctx.req().getMethod() + " " + ctx.path()
                    );
                    throw e;
                }
            }
        });
    }
    
    private static void ensureCsrfToken(Context ctx) {
        HttpSession session = ctx.req().getSession(true);
        String sessionId = session.getId();
        
        String existingToken = ctx.cookie(CSRF_COOKIE_NAME);
        String attrToken = ctx.attribute(CSRF_ATTR_NAME);
        
        if (attrToken != null && !attrToken.isEmpty()) {
            return;
        }
        
        if (existingToken == null || !verifyTokenSignature(existingToken, sessionId)) {
            String newToken = generateCsrfToken();
            String signedToken = signToken(newToken, sessionId);
            
            Cookie cookie = new Cookie(CSRF_COOKIE_NAME, signedToken);
            cookie.setMaxAge((int) TOKEN_EXPIRATION_SECONDS);
            cookie.setHttpOnly(false);
            cookie.setSecure(true);
            cookie.setSameSite(SameSite.STRICT);
            cookie.setPath("/");
            
            ctx.cookie(cookie);
            ctx.attribute(CSRF_ATTR_NAME, signedToken);
        } else {
            ctx.attribute(CSRF_ATTR_NAME, existingToken);
        }
    }
    
    private static void validateCsrfToken(Context ctx) {
        HttpSession session = ctx.req().getSession(false);
        if (session == null) {
            throw new ForbiddenResponse("No session found");
        }
        String sessionId = session.getId();
        
        String cookieToken = ctx.cookie(CSRF_COOKIE_NAME);
        String headerToken = ctx.header(CSRF_HEADER_NAME);
        String formToken = ctx.formParam(CSRF_FORM_FIELD_NAME);
        
        String requestToken = (headerToken != null) ? headerToken : formToken;
        
        if (cookieToken == null || requestToken == null) {
            throw new ForbiddenResponse("CSRF token missing");
        }
        
        if (!verifyTokenSignature(cookieToken, sessionId)) {
            throw new ForbiddenResponse("CSRF token signature invalid");
        }
        
        if (!MessageDigest.isEqual(cookieToken.getBytes(StandardCharsets.UTF_8), 
                                   requestToken.getBytes(StandardCharsets.UTF_8))) {
            throw new ForbiddenResponse("CSRF token mismatch");
        }
    }
    
    private static String generateCsrfToken() {
        byte[] tokenBytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
    
    private static String signToken(String token, String sessionId) {
        try {
            long timestamp = System.currentTimeMillis() / 1000;
            String data = token + "|" + sessionId + "|" + timestamp;
            
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(HMAC_SECRET, "HmacSHA256"));
            byte[] signatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes);
            return token + "." + signature + "|" + timestamp;
            
        } catch (Exception e) {
            throw new RuntimeException("Error signing CSRF token", e);
        }
    }
    
    private static boolean verifyTokenSignature(String signedToken, String sessionId) {
        try {
            String[] parts = signedToken.split("\\.", 2);
            if (parts.length != 2) return false;
            
            String token = parts[0];
            String[] sigParts = parts[1].split("\\|", 2);
            if (sigParts.length != 2) return false;
            
            String receivedSignature = sigParts[0];
            long timestamp = Long.parseLong(sigParts[1]);
            
            long now = System.currentTimeMillis() / 1000;
            if (now - timestamp > TOKEN_EXPIRATION_SECONDS) {
                return false;
            }
            
            String data = token + "|" + sessionId + "|" + timestamp;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(HMAC_SECRET, "HmacSHA256"));
            byte[] expectedSignatureBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String expectedSignature = Base64.getUrlEncoder().withoutPadding().encodeToString(expectedSignatureBytes);
            
            return MessageDigest.isEqual(receivedSignature.getBytes(StandardCharsets.UTF_8), 
                                         expectedSignature.getBytes(StandardCharsets.UTF_8));
            
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean isStateChangingRequest(String method) {
        return "POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || 
               "DELETE".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method);
    }
    
    private static boolean shouldEnsureToken(String path) {
        return !(path.startsWith("/static/") || path.startsWith("/css/") || 
                 path.startsWith("/js/") || path.startsWith("/vendor/") || 
                 path.equals("/favicon.ico"));
    }
    
    private static boolean shouldValidateCsrf(String path) {
        return !(path.equals("/login") || path.equals("/logout") || path.equals("/register") ||
                 path.startsWith("/static/") || path.startsWith("/css/") || 
                 path.startsWith("/js/") || path.startsWith("/vendor/") || 
                 path.equals("/favicon.ico") ||
                 path.startsWith("/api/") || path.startsWith("/webhook/") ||
                 path.equals("/health") || path.equals("/metrics"));
    }
}
