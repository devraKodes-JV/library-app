package com.library.iam.application.service.user;

import com.library.iam.application.dto.UserDTO;
import com.library.iam.application.dto.command.user.UpdateUserCommand;
import com.library.iam.application.validation.UpdateUserCommandValidator;
import com.library.iam.domain.exception.ValidationException;
import com.library.iam.domain.model.Role;
import com.library.iam.domain.model.User;
import com.library.iam.domain.port.out.NotificationService;
import com.library.iam.domain.port.out.RolePort;
import com.library.iam.domain.port.out.UserPort;

public class UpdateUserUseCase {

    private final UserPort userPort;
    private final RolePort rolePort;
    private final NotificationService notificationService;
    private final UpdateUserCommandValidator updateUserCommandValidator;

    public UpdateUserUseCase(UserPort userPort,
                             RolePort rolePort,
                             NotificationService notificationService,
                             UpdateUserCommandValidator updateUserCommandValidator) {
        this.userPort = userPort;
        this.rolePort = rolePort;
        this.notificationService = notificationService;
        this.updateUserCommandValidator = updateUserCommandValidator;
    }

    public UserDTO execute(UpdateUserCommand command) {
        updateUserCommandValidator.validate(command);
        User existing = loadUser(command.id());
        Role role = requireRole(command.roleName());

        existing.setFullName(command.fullName());
        existing.setEmail(command.email());
        existing.setEnabled(command.enabled());
        existing.setRole(role);

        User saved = userPort.save(existing);

        notificationService.publish(com.library.iam.domain.model.NotificationEvent.of(
                "user.updated",
                "User '" + saved.getUsername() + "' updated",
                null,
                null));
        return UserDTO.of(saved);
    }

    private User loadUser(Long id) {
        return userPort.findById(id)
                .orElseThrow(() -> new com.library.iam.domain.exception.UserNotFoundException(id));
    }

    private Role requireRole(String roleName) {
        return rolePort.findByName(roleName)
                .orElseThrow(() -> new com.library.iam.domain.exception.RoleNotFoundException(roleName));
    }
}
