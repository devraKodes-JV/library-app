package com.library.iam.application.service.role;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.library.iam.domain.model.Permission;
import com.library.iam.domain.port.out.PermissionPort;

public class ResolvePermissionsUseCase {

    private final PermissionPort permissionPort;

    public ResolvePermissionsUseCase(PermissionPort permissionPort) {
        this.permissionPort = permissionPort;
    }

    public Set<Permission> execute(List<Long> permissionIds) {
        Set<Permission> permissions = new LinkedHashSet<>();
        if (permissionIds == null) {
            return permissions;
        }
        for (Long id : permissionIds) {
            permissionPort.findById(id).ifPresent(permissions::add);
        }
        return permissions;
    }
}
