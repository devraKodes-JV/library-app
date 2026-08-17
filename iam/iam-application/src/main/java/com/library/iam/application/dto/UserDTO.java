package com.library.iam.application.dto;

import java.time.Instant;

import com.library.iam.domain.model.User;

public record UserDTO(
        Long id,
        String username,
        String fullName,
        String email,
        boolean enabled,
        String roleName,
        Instant deletedAt,
        int failedLoginAttempts,
        Instant lockedUntil) {

    public static UserDTO of(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.isEnabled(),
                user.getRole() != null ? user.getRole().getName() : "",
                user.getDeletedAt(),
                user.getFailedLoginAttempts(),
                user.getLockedUntil());
    }
}
