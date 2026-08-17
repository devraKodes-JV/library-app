package com.library.iam.application.service.user;

import com.library.iam.application.dto.command.user.DisableUserCommand;
import com.library.iam.domain.model.User;
import com.library.iam.domain.port.out.NotificationService;
import com.library.iam.domain.port.out.UserPort;

public class DisableUserUseCase {

    private final UserPort userPort;
    private final NotificationService notificationService;

    public DisableUserUseCase(UserPort userPort, NotificationService notificationService) {
        this.userPort = userPort;
        this.notificationService = notificationService;
    }

    public void execute(DisableUserCommand command) {
        User existing = loadUser(command.id());
        existing.setEnabled(false);
        userPort.save(existing);

        notificationService.publish(com.library.iam.domain.model.NotificationEvent.of(
                "user.disabled",
                "User '" + existing.getUsername() + "' disabled",
                null,
                null));
    }

    private User loadUser(Long id) {
        return userPort.findById(id)
                .orElseThrow(() -> new com.library.iam.domain.exception.UserNotFoundException(id));
    }
}
