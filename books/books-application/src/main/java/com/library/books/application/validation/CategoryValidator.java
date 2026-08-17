package com.library.books.application.validation;

import com.library.books.domain.model.Category;
import com.library.kernel.validation.Validator;

public class CategoryValidator implements Validator<Category> {

    private static final java.util.regex.Pattern CODE_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z0-9]{1,50}$");
    private static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z\\s'-]{2,100}$");

    @Override
    public void validate(Category category) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (category.getCode() == null || category.getCode().isBlank()) {
            errors.put("code", "Code is required.");
        } else if (!CODE_PATTERN.matcher(category.getCode()).matches()) {
            errors.put("code", "Code must be alphanumeric and 50 characters or less.");
        }

        if (category.getName() == null || category.getName().isBlank()) {
            errors.put("name", "Name is required.");
        } else if (!NAME_PATTERN.matcher(category.getName()).matches()) {
            errors.put("name", "Name must contain only letters, spaces, hyphens or apostrophes.");
        }

        if (category.getDescription() != null && category.getDescription().length() > 1000) {
            errors.put("description", "Description must be 1000 characters or less.");
        }

        if (!errors.isEmpty()) {
            throw new com.library.books.domain.exception.ValidationException(errors);
        }
    }
}
