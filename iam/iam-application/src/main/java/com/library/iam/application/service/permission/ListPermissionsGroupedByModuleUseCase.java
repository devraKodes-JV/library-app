package com.library.iam.application.service.permission;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.library.iam.application.dto.PermissionDTO;
import com.library.iam.application.dto.PermissionGroup;
import com.library.iam.domain.model.Permission;
import com.library.iam.domain.port.out.PermissionPort;

public class ListPermissionsGroupedByModuleUseCase {

    private final PermissionPort permissionPort;

    public ListPermissionsGroupedByModuleUseCase(PermissionPort permissionPort) {
        this.permissionPort = permissionPort;
    }

    public List<PermissionGroup> execute() {
        List<Permission> all = permissionPort.findAll();

        Map<Long, PermissionGroup> byModule = new LinkedHashMap<>();
        for (Permission p : all) {
            PermissionGroup group = byModule.computeIfAbsent(
                    p.getModule().getId(),
                    k -> PermissionGroup.of(p.getModule(), new ArrayList<>()));
            group.items().add(PermissionDTO.of(p));
        }

        return byModule.values().stream()
                .map(g -> PermissionGroup.of(g.module(),
                        g.items().stream()
                                .sorted(Comparator.comparing(PermissionDTO::sortOrder))
                                .toList()))
                .sorted(Comparator.comparing(PermissionGroup::sortOrder))
                .toList();
    }
}
