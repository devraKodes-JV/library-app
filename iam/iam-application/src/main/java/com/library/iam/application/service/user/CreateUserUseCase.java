package com.library.iam.application.service.user;

import com.library.iam.application.dto.UserDTO;
import com.library.iam.application.dto.command.user.CreateUserCommand;
import com.library.iam.application.validation.CreateUserCommandValidator;
import com.library.iam.domain.exception.ValidationException;
import com.library.iam.domain.model.Role;
import com.library.iam.domain.model.User;
import com.library.iam.domain.port.out.NotificationService;
import com.library.iam.domain.port.out.PasswordHasher;
import com.library.iam.domain.port.out.RolePort;
import com.library.iam.domain.port.out.UserPort;

public class CreateUserUseCase {

    private final UserPort userPort;
    private final RolePort rolePort;
    private final NotificationService notificationService;
    private final PasswordHasher passwordHasher;
    private final CreateUserCommandValidator createUserCommandValidator;

    public CreateUserUseCase(UserPort userPort,
                             RolePort rolePort,
                             NotificationService notificationService,
                             PasswordHasher passwordHasher,
                             CreateUserCommandValidator createUserCommandValidator) {
        this.userPort = userPort;
        this.rolePort = rolePort;
        this.notificationService = notificationService;
        this.passwordHasher = passwordHasher;
        this.createUserCommandValidator = createUserCommandValidator;
    }

    public UserDTO execute(CreateUserCommand command) {
        createUserCommandValidator.validate(command);
        Role role = requireRole(command.roleName());
        String passwordHash = passwordHasher.hash(command.password());
        User user = User.withoutId(
                command.username(),
                passwordHash,
                command.fullName(),
                command.email(),
                command.enabled(),
                role);
        User saved = userPort.save(user);

        notificationService.publish(com.library.iam.domain.model.NotificationEvent.of(
                "user.created",
                "User '" + saved.getUsername() + "' created",
                null,
                null));
        return UserDTO.of(saved);
    }

    private Role requireRole(String roleName) {
        return rolePort.findByName(roleName)
                .orElseThrow(() -> new com.library.iam.domain.exception.RoleNotFoundException(roleName));
    }
}
