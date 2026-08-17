package com.library.books.application.validation;

import com.library.books.domain.model.BookFormat;
import com.library.kernel.validation.Validator;

public class BookFormatValidator implements Validator<BookFormat> {

    private static final java.util.regex.Pattern CODE_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z0-9]{1,20}$");
    private static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z\\s'-]{2,100}$");

    @Override
    public void validate(BookFormat format) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (format.getCode() == null || format.getCode().isBlank()) {
            errors.put("code", "Code is required.");
        } else if (!CODE_PATTERN.matcher(format.getCode()).matches()) {
            errors.put("code", "Code must be alphanumeric and 20 characters or less.");
        }

        if (format.getName() == null || format.getName().isBlank()) {
            errors.put("name", "Name is required.");
        } else if (!NAME_PATTERN.matcher(format.getName()).matches()) {
            errors.put("name", "Name must contain only letters, spaces, hyphens or apostrophes.");
        }

        if (format.getDescription() != null && format.getDescription().length() > 500) {
            errors.put("description", "Description must be 500 characters or less.");
        }

        if (!errors.isEmpty()) {
            throw new com.library.books.domain.exception.ValidationException(errors);
        }
    }
}
