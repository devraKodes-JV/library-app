package com.library.books.application.validation;

import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.AuthorRole;
import com.library.kernel.validation.Validator;

public class AuthorRoleValidator implements Validator<AuthorRole> {

    @Override
    public void validate(AuthorRole authorRole) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (authorRole.getName() == null || authorRole.getName().isBlank()) {
            errors.put("name", "Name is required.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}
