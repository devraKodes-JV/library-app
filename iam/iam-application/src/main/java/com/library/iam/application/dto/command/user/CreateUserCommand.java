package com.library.iam.application.dto.command.user;

public record CreateUserCommand(
        String username,
        String password,
        String fullName,
        String email,
        boolean enabled,
        String roleName) {
}
