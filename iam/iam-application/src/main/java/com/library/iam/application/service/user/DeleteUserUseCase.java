package com.library.iam.application.service.user;

import com.library.iam.application.dto.command.user.DeleteUserCommand;
import com.library.iam.domain.model.User;
import com.library.iam.domain.port.out.NotificationService;
import com.library.iam.domain.port.out.UserPort;

public class DeleteUserUseCase {

    private final UserPort userPort;
    private final NotificationService notificationService;

    public DeleteUserUseCase(UserPort userPort, NotificationService notificationService) {
        this.userPort = userPort;
        this.notificationService = notificationService;
    }

    public void execute(DeleteUserCommand command) {
        User existing = loadUser(command.id());
        userPort.delete(command.id());

        notificationService.publish(com.library.iam.domain.model.NotificationEvent.of(
                "user.deleted",
                "User '" + existing.getUsername() + "' deleted",
                null,
                null));
    }

    private User loadUser(Long id) {
        return userPort.findById(id)
                .orElseThrow(() -> new com.library.iam.domain.exception.UserNotFoundException(id));
    }
}
