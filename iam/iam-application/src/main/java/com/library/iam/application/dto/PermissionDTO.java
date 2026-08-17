package com.library.iam.application.dto;

import com.library.iam.domain.model.Module;
import com.library.iam.domain.model.Permission;

public record PermissionDTO(
        Long id,
        String code,
        String name,
        String menuLabel,
        String icon,
        String url,
        Integer sortOrder,
        boolean enabled,
        String moduleCode,
        String moduleName) {

    public static PermissionDTO of(Permission permission) {
        Module module = permission.getModule();
        return new PermissionDTO(
                permission.getId(),
                permission.getCode(),
                permission.getName(),
                permission.getMenuLabel(),
                permission.getIcon(),
                permission.getUrl(),
                permission.getSortOrder(),
                permission.isEnabled(),
                module != null ? module.getCode() : "",
                module != null ? module.getName() : "");
    }
}
