package com.library.iam.application.dto.command.user;

public record UpdateUserCommand(
        Long id,
        String fullName,
        String email,
        boolean enabled,
        String roleName) {
}
