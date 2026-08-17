package com.library.books.application.validation;

import com.library.books.domain.model.Author;
import com.library.kernel.validation.Validator;

public class AuthorValidator implements Validator<Author> {

    private static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z\\s'-]{2,100}$");
    private static final java.util.regex.Pattern BIOGRAPHY_PATTERN = java.util.regex.Pattern.compile("^[a-zA-ZÀ-ÿ0-9\\s.,!?'\"-]*$");

    @Override
    public void validate(Author author) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (author.getFirstName() == null || author.getFirstName().isBlank()) {
            errors.put("firstName", "First name is required.");
        } else if (!NAME_PATTERN.matcher(author.getFirstName()).matches()) {
            errors.put("firstName", "First name must contain only letters, spaces, hyphens or apostrophes.");
        }

        if (author.getLastName() == null || author.getLastName().isBlank()) {
            errors.put("lastName", "Last name is required.");
        } else if (!NAME_PATTERN.matcher(author.getLastName()).matches()) {
            errors.put("lastName", "Last name must contain only letters, spaces, hyphens or apostrophes.");
        }

        if (author.getBiography() != null && author.getBiography().length() > 2000) {
            errors.put("biography", "Biography must be 2000 characters or less.");
        } else if (author.getBiography() != null && !BIOGRAPHY_PATTERN.matcher(author.getBiography()).matches()) {
            errors.put("biography", "Biography contains invalid characters.");
        }

        if (author.getBirthDate() != null && !author.getBirthDate().isBlank()) {
            try {
                java.time.LocalDate birth = java.time.LocalDate.parse(author.getBirthDate());
                if (birth.isAfter(java.time.LocalDate.now())) {
                    errors.put("birthDate", "Birth date cannot be in the future.");
                }
                if (birth.isAfter(java.time.LocalDate.now().minusYears(18))) {
                    errors.put("birthDate", "Author must be at least 18 years old.");
                }
            } catch (java.time.format.DateTimeParseException e) {
                errors.put("birthDate", "Invalid birth date format. Use YYYY-MM-DD.");
            }
        }

        if (author.getDeathDate() != null && !author.getDeathDate().isBlank()) {
            try {
                java.time.LocalDate death = java.time.LocalDate.parse(author.getDeathDate());
                if (death.isAfter(java.time.LocalDate.now())) {
                    errors.put("deathDate", "Death date cannot be in the future.");
                }
                if (author.getBirthDate() != null && !author.getBirthDate().isBlank()) {
                    try {
                        java.time.LocalDate birth = java.time.LocalDate.parse(author.getBirthDate());
                        if (death.isBefore(birth)) {
                            errors.put("deathDate", "Death date cannot be before birth date.");
                        }
                    } catch (java.time.format.DateTimeParseException ignored) {
                    }
                }
            } catch (java.time.format.DateTimeParseException e) {
                errors.put("deathDate", "Invalid death date format. Use YYYY-MM-DD.");
            }
        }

        if (!errors.isEmpty()) {
            throw new com.library.books.domain.exception.ValidationException(errors);
        }
    }
}
