package com.library.iam.application.service.permission;

import java.util.Comparator;
import java.util.List;

import com.library.iam.application.dto.PermissionDTO;
import com.library.iam.domain.model.Permission;
import com.library.iam.domain.port.out.PermissionPort;

public class ListPermissionsFlatUseCase {

    private final PermissionPort permissionPort;

    public ListPermissionsFlatUseCase(PermissionPort permissionPort) {
        this.permissionPort = permissionPort;
    }

    public List<PermissionDTO> execute() {
        return permissionPort.findAll().stream()
                .map(PermissionDTO::of)
                .sorted(Comparator.comparing(PermissionDTO::sortOrder))
                .toList();
    }
}
