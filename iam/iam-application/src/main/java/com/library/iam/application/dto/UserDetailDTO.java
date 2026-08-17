package com.library.iam.application.dto;

import java.time.Instant;
import java.util.List;

import com.library.iam.domain.model.Role;
import com.library.iam.domain.model.User;

public record UserDetailDTO(
        Long id,
        String username,
        String fullName,
        String email,
        boolean enabled,
        String roleName,
        Instant deletedAt,
        int failedLoginAttempts,
        Instant lockedUntil,
        List<RoleDTO> roles) {

    public static UserDetailDTO from(User user, List<RoleDTO> roles) {
        return new UserDetailDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.isEnabled(),
                user.getRole() != null ? user.getRole().getName() : "",
                user.getDeletedAt(),
                user.getFailedLoginAttempts(),
                user.getLockedUntil(),
                roles != null ? roles : List.of());
    }
}
