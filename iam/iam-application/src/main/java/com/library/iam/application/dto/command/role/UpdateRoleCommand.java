package com.library.iam.application.dto.command.role;

import java.util.List;

public record UpdateRoleCommand(
        Long id,
        String name,
        String description,
        List<Long> permissionIds) {
}
