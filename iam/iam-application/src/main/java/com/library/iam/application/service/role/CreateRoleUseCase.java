package com.library.iam.application.service.role;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.library.iam.application.dto.RoleDTO;
import com.library.iam.application.dto.command.role.CreateRoleCommand;
import com.library.iam.application.validation.CreateRoleCommandValidator;
import com.library.iam.domain.exception.ValidationException;
import com.library.iam.domain.model.Permission;
import com.library.iam.domain.model.Role;
import com.library.iam.domain.port.out.NotificationService;
import com.library.iam.domain.port.out.PermissionPort;
import com.library.iam.domain.port.out.RolePort;

public class CreateRoleUseCase {

    private final RolePort rolePort;
    private final PermissionPort permissionPort;
    private final NotificationService notificationService;
    private final CreateRoleCommandValidator createRoleCommandValidator;

    public CreateRoleUseCase(RolePort rolePort,
                             PermissionPort permissionPort,
                             NotificationService notificationService,
                             CreateRoleCommandValidator createRoleCommandValidator) {
        this.rolePort = rolePort;
        this.permissionPort = permissionPort;
        this.notificationService = notificationService;
        this.createRoleCommandValidator = createRoleCommandValidator;
    }

    public RoleDTO execute(CreateRoleCommand command) {
        createRoleCommandValidator.validate(command);
        Role role = Role.withoutId(normaliseName(command.name()), command.description());
        for (Permission perm : resolve(command.permissionIds())) {
            role.addPermission(perm);
        }
        Role saved = rolePort.save(role);

        notificationService.publish(com.library.iam.domain.model.NotificationEvent.of(
                "role.created",
                "Role '" + saved.getName() + "' created",
                null,
                null));
        return RoleDTO.of(saved);
    }

    private Set<Permission> resolve(List<Long> permissionIds) {
        Set<Permission> permissions = new LinkedHashSet<>();
        if (permissionIds == null) {
            return permissions;
        }
        for (Long id : permissionIds) {
            permissionPort.findById(id).ifPresent(permissions::add);
        }
        return permissions;
    }

    private String normaliseName(String name) {
        return name == null ? "" : name.trim().toUpperCase();
    }
}
