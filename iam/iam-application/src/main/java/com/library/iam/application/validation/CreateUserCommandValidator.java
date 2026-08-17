package com.library.iam.application.validation;

import com.library.iam.application.dto.command.user.CreateUserCommand;
import com.library.iam.domain.exception.ValidationException;
import com.library.kernel.validation.Validator;

public class CreateUserCommandValidator implements Validator<CreateUserCommand> {

    private static final java.util.regex.Pattern USERNAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
    private static final java.util.regex.Pattern EMAIL_PATTERN = java.util.regex.Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z\\s'-]{2,100}$");

    @Override
    public void validate(CreateUserCommand command) {
        var errors = new java.util.LinkedHashMap<String, String>();
        String username = command.username();
        String password = command.password();
        String fullName = command.fullName();
        String email = command.email();
        String roleName = command.roleName();

        if (username == null || username.isBlank()) {
            errors.put("username", "Username is required.");
        } else if (!USERNAME_PATTERN.matcher(username).matches()) {
            errors.put("username", "Username must be 3-50 characters, alphanumeric (underscores allowed).");
        }

        if (password == null || password.isBlank()) {
            errors.put("password", "Password is required.");
        } else if (password.length() < 12 || password.length() > 128) {
            errors.put("password", "Password must be 12-128 characters.");
        } else {
            if (!password.chars().anyMatch(Character::isUpperCase)) {
                errors.put("password", "Password must contain uppercase letters.");
            }
            if (!password.chars().anyMatch(Character::isLowerCase)) {
                errors.put("password", "Password must contain lowercase letters.");
            }
            if (!password.chars().anyMatch(Character::isDigit)) {
                errors.put("password", "Password must contain digits.");
            }
            if (password.matches("[a-zA-Z0-9]*")) {
                errors.put("password", "Password must contain special characters.");
            }
        }

        if (fullName == null || fullName.isBlank()) {
            errors.put("fullName", "Full name is required.");
        } else if (!NAME_PATTERN.matcher(fullName).matches()) {
            errors.put("fullName", "Full name must be 2-100 characters, letters and spaces only.");
        }

        if (email == null || email.isBlank()) {
            errors.put("email", "Email is required.");
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.put("email", "Invalid email format.");
        }

        if (roleName == null || roleName.isBlank()) {
            errors.put("roleName", "Role name is required.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
