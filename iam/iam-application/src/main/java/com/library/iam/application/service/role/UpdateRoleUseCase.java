package com.library.iam.application.service.role;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.library.iam.application.dto.RoleDTO;
import com.library.iam.application.dto.command.role.UpdateRoleCommand;
import com.library.iam.application.validation.UpdateRoleCommandValidator;
import com.library.iam.domain.exception.ValidationException;
import com.library.iam.domain.exception.RoleNotFoundException;
import com.library.iam.domain.model.Permission;
import com.library.iam.domain.model.Role;
import com.library.iam.domain.port.out.NotificationService;
import com.library.iam.domain.port.out.PermissionPort;
import com.library.iam.domain.port.out.RolePort;

public class UpdateRoleUseCase {

    private final RolePort rolePort;
    private final PermissionPort permissionPort;
    private final NotificationService notificationService;
    private final UpdateRoleCommandValidator updateRoleCommandValidator;

    public UpdateRoleUseCase(RolePort rolePort,
                             PermissionPort permissionPort,
                             NotificationService notificationService,
                             UpdateRoleCommandValidator updateRoleCommandValidator) {
        this.rolePort = rolePort;
        this.permissionPort = permissionPort;
        this.notificationService = notificationService;
        this.updateRoleCommandValidator = updateRoleCommandValidator;
    }

    public RoleDTO execute(UpdateRoleCommand command) {
        updateRoleCommandValidator.validate(command);
        Role existing = loadRole(command.id());

        existing.setName(normaliseName(command.name()));
        existing.setDescription(command.description());
        existing.getPermissions().clear();
        for (Permission perm : resolve(command.permissionIds())) {
            existing.addPermission(perm);
        }

        Role saved = rolePort.save(existing);

        notificationService.publish(com.library.iam.domain.model.NotificationEvent.of(
                "role.updated",
                "Role '" + saved.getName() + "' updated",
                null,
                null));
        return RoleDTO.of(saved);
    }

    private Role loadRole(Long id) {
        return rolePort.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    private Set<Permission> resolve(List<Long> permissionIds) {
        Set<Permission> permissions = new LinkedHashSet<>();
        if (permissionIds == null) {
            return permissions;
        }
        for (Long pid : permissionIds) {
            permissionPort.findById(pid).ifPresent(permissions::add);
        }
        return permissions;
    }

    private String normaliseName(String name) {
        return name == null ? "" : name.trim().toUpperCase();
    }
}
