package com.library.iam.application.dto;

import java.util.List;

import com.library.iam.domain.model.Permission;
import com.library.iam.domain.model.Role;

public record RoleDetailDTO(
        Long id,
        String name,
        String description,
        boolean enabled,
        List<PermissionDTO> permissions) {

    public static RoleDetailDTO from(Role role, List<PermissionDTO> permissions) {
        return new RoleDetailDTO(
                role.getId(),
                role.getName(),
                role.getDescription(),
                role.isEnabled(),
                permissions != null ? permissions : List.of());
    }
}
