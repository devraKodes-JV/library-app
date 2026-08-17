package com.library.iam.application.service.user;

import com.library.iam.application.dto.command.user.ReinstateUserCommand;
import com.library.iam.domain.port.out.NotificationService;
import com.library.iam.domain.port.out.UserPort;

public class ReinstateUserUseCase {

    private final UserPort userPort;
    private final NotificationService notificationService;

    public ReinstateUserUseCase(UserPort userPort, NotificationService notificationService) {
        this.userPort = userPort;
        this.notificationService = notificationService;
    }

    public void execute(ReinstateUserCommand command) {
        userPort.reinstate(command.id());

        notificationService.publish(com.library.iam.domain.model.NotificationEvent.of(
                "user.reinstated",
                "User with id " + command.id() + " reinstated",
                null,
                null));
    }
}
