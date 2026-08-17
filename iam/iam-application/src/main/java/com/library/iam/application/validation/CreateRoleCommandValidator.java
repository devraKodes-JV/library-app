package com.library.iam.application.validation;

import com.library.iam.application.dto.command.role.CreateRoleCommand;
import com.library.iam.domain.exception.ValidationException;
import com.library.kernel.validation.Validator;

public class CreateRoleCommandValidator implements Validator<CreateRoleCommand> {

    private static final java.util.regex.Pattern ROLE_NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z0-9\\s]{2,50}$");
    private static final int MAX_DESCRIPTION_LENGTH = 500;

    @Override
    public void validate(CreateRoleCommand command) {
        var errors = new java.util.LinkedHashMap<String, String>();
        String name = command.name();
        String description = command.description();

        if (name == null || name.isBlank()) {
            errors.put("name", "Role name is required.");
        } else if (!ROLE_NAME_PATTERN.matcher(name).matches()) {
            errors.put("name", "Role name must be 2-50 characters, alphanumeric and spaces only.");
        }

        if (description != null && description.length() > MAX_DESCRIPTION_LENGTH) {
            errors.put("description", "Description must be under " + MAX_DESCRIPTION_LENGTH + " characters.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
