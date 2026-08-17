package com.library.iam.application.dto;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.library.iam.domain.model.Permission;
import com.library.iam.domain.model.Role;

public record RoleDTO(
        Long id,
        String name,
        String description,
        boolean enabled,
        List<Long> permissionIds) {

    public static RoleDTO of(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.isEnabled(),
                role.getPermissions() != null
                        ? role.getPermissions().stream().map(Permission::getId).filter(java.util.Objects::nonNull).toList()
                        : List.of());
    }
}
