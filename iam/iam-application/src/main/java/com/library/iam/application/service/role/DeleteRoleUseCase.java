package com.library.iam.application.service.role;

import com.library.iam.application.dto.command.role.DeleteRoleCommand;
import com.library.iam.domain.exception.RoleNotFoundException;
import com.library.iam.domain.model.Role;
import com.library.iam.domain.port.out.NotificationService;
import com.library.iam.domain.port.out.RolePort;

public class DeleteRoleUseCase {

    private final RolePort rolePort;
    private final NotificationService notificationService;

    public DeleteRoleUseCase(RolePort rolePort, NotificationService notificationService) {
        this.rolePort = rolePort;
        this.notificationService = notificationService;
    }

    public void execute(DeleteRoleCommand command) {
        Role existing = loadRole(command.id());
        rolePort.delete(command.id());

        notificationService.publish(com.library.iam.domain.model.NotificationEvent.of(
                "role.deleted",
                "Role '" + existing.getName() + "' deleted",
                null,
                null));
    }

    private Role loadRole(Long id) {
        return rolePort.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }
}
