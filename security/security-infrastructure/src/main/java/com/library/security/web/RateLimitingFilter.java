package com.library.security.web;

import com.library.security.infrastructure.cache.ExpiringCache;
import com.library.security.service.SecurityAuditService;

import io.javalin.http.Context;
import io.javalin.config.JavalinConfig;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;

public final class RateLimitingFilter {
    
    private static final int IP_REQUESTS_PER_MINUTE = 20;
    private static final int USER_REQUESTS_PER_MINUTE = 10;
    private static final long TIME_WINDOW_SECONDS = 60;
    private static final long CLEANUP_INTERVAL_SECONDS = 300;
    
    private static final ExpiringCache<String, RateLimitCounter> ipRateLimitStore = 
        new ExpiringCache<>(TIME_WINDOW_SECONDS, CLEANUP_INTERVAL_SECONDS);
    private static final ExpiringCache<String, RateLimitCounter> userRateLimitStore = 
        new ExpiringCache<>(TIME_WINDOW_SECONDS, CLEANUP_INTERVAL_SECONDS);
    
    private static final String[] TRUSTED_PROXIES = {
        "10.0.0.0/8", "172.16.0.0/12", "192.168.0.0/16", "127.0.0.1"
    };
    
    private static final ConcurrentHashMap<String, Object> keyLocks = new ConcurrentHashMap<>();
    
    private RateLimitingFilter() {
    }
    
    public static void register(JavalinConfig config, SecurityAuditService auditService) {
        config.routes.before(ctx -> {
            String clientIp = getClientIp(ctx);
            String path = ctx.path();
            String method = ctx.req().getMethod();
            
            if (isAuthenticated(ctx)) {
                return;
            }
            
            if (isPublicSensitivePath(method, path)) {
                if (!checkIpRateLimit(clientIp)) {
                    auditService.audit(
                        "RATE_LIMIT_EXCEEDED",
                        null,
                        clientIp,
                        "IP rate limit exceeded for " + method + " " + path
                    );
                    ctx.status(429);
                    long remaining = remainingSeconds(clientIp, ipRateLimitStore, IP_REQUESTS_PER_MINUTE);
                    ctx.header("Retry-After", String.valueOf(remaining));
                    ctx.html(rateLimitHtml(path, remaining));
                    ctx.skipRemainingHandlers();
                    return;
                }
                
                if ("POST".equalsIgnoreCase(method) && "/login".equals(path)) {
                    String username = ctx.formParam("username");
                    if (username != null && !username.isEmpty() && !checkUserRateLimit(username)) {
                        auditService.audit(
                            "RATE_LIMIT_EXCEEDED",
                            username,
                            clientIp,
                            "User rate limit exceeded for login"
                        );
                        ctx.status(429);
                        long remaining = remainingSeconds("user:" + username, userRateLimitStore, USER_REQUESTS_PER_MINUTE);
                        ctx.header("Retry-After", String.valueOf(remaining));
                        ctx.html(rateLimitHtml(path, remaining));
                        ctx.skipRemainingHandlers();
                        return;
                    }
                }
            }
        });
    }
    
    private static boolean isAuthenticated(Context ctx) {
        return ctx.sessionAttribute("user") != null;
    }
    
    private static boolean isPublicSensitivePath(String method, String path) {
        if (path.startsWith("/static/") || path.startsWith("/css/") 
            || path.startsWith("/js/") || path.startsWith("/vendor/") || path.equals("/favicon.ico")) {
            return false;
        }
        return true;
    }
    
    private static boolean checkIpRateLimit(String clientIp) {
        return checkRateLimit(ipRateLimitStore, clientIp, IP_REQUESTS_PER_MINUTE);
    }
    
    private static boolean checkUserRateLimit(String username) {
        return checkRateLimit(userRateLimitStore, "user:" + username, USER_REQUESTS_PER_MINUTE);
    }
    
    private static boolean checkRateLimit(ExpiringCache<String, RateLimitCounter> cache, String key, int maxRequests) {
        Object lock = keyLocks.computeIfAbsent(key, k -> new Object());
        synchronized (lock) {
            RateLimitCounter counter = cache.get(key);
            
            if (counter == null) {
                counter = new RateLimitCounter(System.currentTimeMillis(), new AtomicInteger(0));
                cache.put(key, counter);
            }
            
            long now = System.currentTimeMillis();
            if (now - counter.getWindowStart() > TIME_WINDOW_SECONDS * 1000) {
                counter.reset(now);
            }
            
            int current = counter.getCount().incrementAndGet();
            return current <= maxRequests;
        }
    }

    private static long remainingSeconds(String key, ExpiringCache<String, RateLimitCounter> cache, int maxRequests) {
        RateLimitCounter counter = cache.get(key);
        if (counter == null) {
            return TIME_WINDOW_SECONDS;
        }
        long elapsed = (System.currentTimeMillis() - counter.getWindowStart()) / 1000;
        return Math.max(1, TIME_WINDOW_SECONDS - elapsed);
    }
    
    private static String getClientIp(Context ctx) {
        if (isFromTrustedProxy(ctx)) {
            String xForwardedFor = ctx.header("X-Forwarded-For");
            if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
                return xForwardedFor.split(",")[0].trim();
            }
        }
        String xRealIp = ctx.header("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        return ctx.ip();
    }
    
    private static boolean isFromTrustedProxy(Context ctx) {
        String remoteAddr = ctx.req().getRemoteAddr();
        for (String trustedRange : TRUSTED_PROXIES) {
            if (isInRange(remoteAddr, trustedRange)) {
                return true;
            }
        }
        return false;
    }
    
    private static boolean isInRange(String ip, String cidr) {
        try {
            String[] cidrParts = cidr.split("/");
            String cidrIp = cidrParts[0];
            int prefixLength = Integer.parseInt(cidrParts[1]);
            
            long ipLong = ipToLong(ip);
            long cidrLong = ipToLong(cidrIp);
            long mask = -1L << (32 - prefixLength);
            
            return (ipLong & mask) == (cidrLong & mask);
        } catch (Exception e) {
            return false;
        }
    }
    
    private static long ipToLong(String ip) {
        String[] parts = ip.split("\\.");
        return (Long.parseLong(parts[0]) << 24) +
               (Long.parseLong(parts[1]) << 16) +
               (Long.parseLong(parts[2]) << 8) +
               Long.parseLong(parts[3]);
    }
    
    private static String rateLimitHtml(String path, long remainingSeconds) {
        String template = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Library - Too Many Requests</title>
                <link rel="stylesheet" href="/css/bootstrap.min.css">
                <link rel="stylesheet" href="/css/bootstrap-icons.min.css">
                <link rel="stylesheet" href="/css/app.base.css">
                <style>
                    .countdown-number { font-size: 2.5rem; font-weight: bold; color: #ffc107; }
                </style>
            </head>
            <body class="login-page">
                <div class="login-card">
                    <div class="login-brand">
                        <i class="bi bi-shield-lock"></i>
                        <h1>Library</h1>
                        <p>Too Many Requests</p>
                    </div>
                    <div class="alert alert-warning" role="alert">
                        <i class="bi bi-exclamation-triangle"></i>
                        You have made too many requests. Please wait a moment and try again.
                    </div>
                    <div class="text-center my-4">
                        <p class="text-muted mb-2">Retrying in:</p>
                        <span id="countdown" class="countdown-number">%d</span> <span class="text-muted">seconds</span>
                    </div>
                    <div class="text-center mt-3">
                        <a href="/login" class="btn btn-primary" id="retry-link" style="display: none;">Return to Login</a>
                    </div>
                </div>
                <script>
                    var secondsLeft = %d;
                    var countdownEl = document.getElementById('countdown');
                    var retryLink = document.getElementById('retry-link');
                    var timer = setInterval(function() {
                        secondsLeft--;
                        if (secondsLeft <= 0) {
                            clearInterval(timer);
                            countdownEl.textContent = '0';
                            retryLink.style.display = 'inline-block';
                            window.location.reload();
                        } else {
                            countdownEl.textContent = secondsLeft;
                        }
                    }, 1000);
                </script>
            </body>
            </html>
            """;
        return String.format(template, remainingSeconds, remainingSeconds);
    }
    
    private static class RateLimitCounter {
        private volatile long windowStart;
        private final AtomicInteger count;
        
        RateLimitCounter(long windowStart, AtomicInteger count) {
            this.windowStart = windowStart;
            this.count = count;
        }
        
        long getWindowStart() {
            return windowStart;
        }
        
        AtomicInteger getCount() {
            return count;
        }
        
        void reset(long newWindowStart) {
            this.windowStart = newWindowStart;
            this.count.set(1);
        }
    }
}
