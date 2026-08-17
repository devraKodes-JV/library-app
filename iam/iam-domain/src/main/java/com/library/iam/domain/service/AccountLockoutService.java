package com.library.iam.domain.service;

import java.time.Instant;

public final class AccountLockoutService {
    
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCKOUT_DURATION_MINUTES = 15;
    
    private AccountLockoutService() {
    }
    
    public static boolean isLocked(int failedAttempts, Instant lockedUntil) {
        if (failedAttempts >= MAX_FAILED_ATTEMPTS && lockedUntil != null) {
            return Instant.now().isBefore(lockedUntil);
        }
        return false;
    }
    
    public static FailedResult recordFailedAttempt(int currentAttempts, Instant currentLockedUntil) {
        int newAttempts = currentAttempts + 1;
        Instant newLockedUntil = currentLockedUntil;
        
        if (newAttempts >= MAX_FAILED_ATTEMPTS) {
            newLockedUntil = Instant.now().plusSeconds(LOCKOUT_DURATION_MINUTES * 60);
        }
        
        return new FailedResult(newAttempts, newLockedUntil);
    }
    
    public static ResetResult resetFailedAttempts() {
        return new ResetResult(0, null);
    }
    
    public static int getMaxFailedAttempts() {
        return MAX_FAILED_ATTEMPTS;
    }
    
    public static long getLockoutDurationMinutes() {
        return LOCKOUT_DURATION_MINUTES;
    }
    
    public record FailedResult(int failedAttempts, Instant lockedUntil) {
    }
    
    public record ResetResult(int failedAttempts, Instant lockedUntil) {
    }
}
