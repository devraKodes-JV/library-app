package com.library.books.application.validation;

import com.library.books.domain.model.Edition;
import com.library.kernel.validation.Validator;

public class EditionValidator implements Validator<Edition> {

    private static final java.util.regex.Pattern ISBN_PATTERN = java.util.regex.Pattern.compile("^\\d{10}$|^\\d{13}$|^\\d{9}X$");
    private static final java.util.regex.Pattern ALPHANUMERIC_PATTERN = java.util.regex.Pattern.compile("^[a-zA-ZÀ-ÿ0-9\\s'-]+$");

    @Override
    public void validate(Edition edition) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (edition.getEditionNumber() == null || edition.getEditionNumber().isBlank()) {
            errors.put("editionNumber", "Edition number is required.");
        } else if (!ALPHANUMERIC_PATTERN.matcher(edition.getEditionNumber()).matches()) {
            errors.put("editionNumber", "Edition number must contain only letters, numbers, spaces, hyphens or apostrophes.");
        }

        if (edition.getIsbn() != null && !edition.getIsbn().isBlank()) {
            String cleaned = edition.getIsbn().replaceAll("[\\s-]", "");
            if (!ISBN_PATTERN.matcher(cleaned).matches()) {
                errors.put("isbn", "ISBN must be 10 or 13 digits (optionally ending with X).");
            }
        }

        if (edition.getPages() != null && edition.getPages() <= 0) {
            errors.put("pages", "Pages must be a positive number.");
        }
        if (edition.getPages() != null && edition.getPages() > 9999) {
            errors.put("pages", "Pages must be 9999 or less.");
        }

        if (edition.getPublicationYear() != null) {
            int currentYear = java.time.LocalDate.now().getYear();
            if (edition.getPublicationYear() < 1450) {
                errors.put("publicationYear", "Publication year must be 1450 or later.");
            }
            if (edition.getPublicationYear() > currentYear + 5) {
                errors.put("publicationYear", "Publication year cannot be more than 5 years in the future.");
            }
        }

        if (!errors.isEmpty()) {
            throw new com.library.books.domain.exception.ValidationException(errors);
        }
    }
}
