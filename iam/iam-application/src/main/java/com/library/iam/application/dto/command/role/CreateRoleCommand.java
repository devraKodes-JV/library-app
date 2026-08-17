package com.library.iam.application.dto.command.role;

import java.util.List;

public record CreateRoleCommand(
        String name,
        String description,
        List<Long> permissionIds) {
}
