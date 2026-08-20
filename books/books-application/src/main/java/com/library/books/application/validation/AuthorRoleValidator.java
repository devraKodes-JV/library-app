package com.library.books.application.validation;

import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.AuthorRole;
import com.library.kernel.validation.Validator;

public class AuthorRoleValidator implements Validator<AuthorRole> {

    private static final java.util.regex.Pattern ALPHANUMERIC_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z0-9\\s'-]+$");

    @Override
    public void validate(AuthorRole authorRole) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (authorRole.getCode() == null || authorRole.getCode().isBlank()) {
            errors.put("code", "Code is required.");
        } else if (!ALPHANUMERIC_PATTERN.matcher(authorRole.getCode()).matches()) {
            errors.put("code", "Code must contain only letters, numbers, underscores, hyphens or apostrophes.");
        }

        if (authorRole.getName() == null || authorRole.getName().isBlank()) {
            errors.put("name", "Name is required.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
