package com.library.iam.application.service.role;

import com.library.iam.application.dto.command.role.ReinstateRoleCommand;
import com.library.iam.domain.port.out.NotificationService;
import com.library.iam.domain.port.out.RolePort;

public class ReinstateRoleUseCase {

    private final RolePort rolePort;
    private final NotificationService notificationService;

    public ReinstateRoleUseCase(RolePort rolePort, NotificationService notificationService) {
        this.rolePort = rolePort;
        this.notificationService = notificationService;
    }

    public void execute(ReinstateRoleCommand command) {
        rolePort.reinstate(command.id());

        notificationService.publish(com.library.iam.domain.model.NotificationEvent.of(
                "role.reinstated",
                "Role with id " + command.id() + " reinstated",
                null,
                null));
    }
}
