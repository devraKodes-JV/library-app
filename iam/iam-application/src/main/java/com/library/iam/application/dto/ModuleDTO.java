package com.library.iam.application.dto;

import com.library.iam.domain.model.Module;

public record ModuleDTO(
        Long id,
        String code,
        String name,
        String menuLabel,
        String icon,
        Integer sortOrder,
        boolean enabled) {

    public static ModuleDTO of(Module module) {
        return new ModuleDTO(
                module.getId(),
                module.getCode(),
                module.getName(),
                module.getMenuLabel(),
                module.getIcon(),
                module.getSortOrder(),
                module.isEnabled());
    }
}
