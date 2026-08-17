package com.library.iam.application.dto;

import java.util.List;

import com.library.iam.domain.model.Module;

public record PermissionGroup(
        Module module,
        List<PermissionDTO> items) {

    public static PermissionGroup of(Module module, List<PermissionDTO> items) {
        return new PermissionGroup(module, items);
    }

    public Integer sortOrder() {
        return module.getSortOrder();
    }
}
