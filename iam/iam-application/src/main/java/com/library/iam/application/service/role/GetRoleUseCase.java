package com.library.iam.application.service.role;

import com.library.iam.application.dto.RoleDTO;
import com.library.iam.domain.exception.RoleNotFoundException;
import com.library.iam.domain.port.out.RolePort;

public class GetRoleUseCase {

    private final RolePort rolePort;

    public GetRoleUseCase(RolePort rolePort) {
        this.rolePort = rolePort;
    }

    public RoleDTO execute(Long id) {
        var role = rolePort.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        return RoleDTO.of(role);
    }
}
