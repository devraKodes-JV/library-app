package com.library.books.application.validation;

import com.library.books.domain.model.Language;
import com.library.kernel.validation.Validator;

public class LanguageValidator implements Validator<Language> {

    private static final java.util.regex.Pattern CODE_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z0-9]{1,20}$");
    private static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z\\s'-]{2,100}$");

    @Override
    public void validate(Language language) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (language.getCode() == null || language.getCode().isBlank()) {
            errors.put("code", "Code is required.");
        } else if (!CODE_PATTERN.matcher(language.getCode()).matches()) {
            errors.put("code", "Code must be alphanumeric and 20 characters or less.");
        }

        if (language.getName() == null || language.getName().isBlank()) {
            errors.put("name", "Name is required.");
        } else if (!NAME_PATTERN.matcher(language.getName()).matches()) {
            errors.put("name", "Name must contain only letters, spaces, hyphens or apostrophes.");
        }

        if (!errors.isEmpty()) {
            throw new com.library.books.domain.exception.ValidationException(errors);
        }
    }
}
