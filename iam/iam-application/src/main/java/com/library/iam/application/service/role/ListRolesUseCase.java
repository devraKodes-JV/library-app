package com.library.iam.application.service.role;

import java.util.List;

import com.library.iam.application.dto.RoleDTO;
import com.library.iam.domain.port.out.RolePort;

public class ListRolesUseCase {

    private final RolePort rolePort;

    public ListRolesUseCase(RolePort rolePort) {
        this.rolePort = rolePort;
    }

    public List<RoleDTO> execute() {
        return rolePort.findAll().stream()
                .map(RoleDTO::of)
                .toList();
    }
}
