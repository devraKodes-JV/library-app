package com.library.iam.application.validation;

import com.library.iam.application.dto.command.user.UpdateUserCommand;
import com.library.iam.domain.exception.ValidationException;
import com.library.kernel.validation.Validator;

public class UpdateUserCommandValidator implements Validator<UpdateUserCommand> {

    private static final java.util.regex.Pattern EMAIL_PATTERN = java.util.regex.Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z\\s'-]{2,100}$");

    @Override
    public void validate(UpdateUserCommand command) {
        var errors = new java.util.LinkedHashMap<String, String>();
        String fullName = command.fullName();
        String email = command.email();
        String roleName = command.roleName();

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
