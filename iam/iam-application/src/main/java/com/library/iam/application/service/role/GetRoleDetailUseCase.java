package com.library.iam.application.service.role;

import java.util.List;

import com.library.iam.application.dto.PermissionDTO;
import com.library.iam.application.dto.RoleDetailDTO;
import com.library.iam.domain.exception.RoleNotFoundException;
import com.library.iam.domain.model.Permission;
import com.library.iam.domain.model.Role;
import com.library.iam.domain.port.out.RolePort;

public class GetRoleDetailUseCase {

    private final RolePort rolePort;

    public GetRoleDetailUseCase(RolePort rolePort) {
        this.rolePort = rolePort;
    }

    public RoleDetailDTO execute(Long id) {
        Role role = rolePort.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
        List<PermissionDTO> permissions = role.getPermissions() != null
                ? role.getPermissions().stream().map(PermissionDTO::of).toList()
                : List.of();
        return RoleDetailDTO.from(role, permissions);
    }
}
