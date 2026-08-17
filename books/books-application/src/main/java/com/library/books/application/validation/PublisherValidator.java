package com.library.books.application.validation;

import com.library.books.domain.model.Publisher;
import com.library.kernel.validation.Validator;

public class PublisherValidator implements Validator<Publisher> {

    private static final java.util.regex.Pattern NAME_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z\\s'-]{2,100}$");
    private static final java.util.regex.Pattern COUNTRY_PATTERN = java.util.regex.Pattern.compile("^[a-zA-Z\\s'-]{2,100}$");
    private static final java.util.regex.Pattern URL_PATTERN = java.util.regex.Pattern.compile("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$");

    @Override
    public void validate(Publisher publisher) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (publisher.getName() == null || publisher.getName().isBlank()) {
            errors.put("name", "Name is required.");
        } else if (!NAME_PATTERN.matcher(publisher.getName()).matches()) {
            errors.put("name", "Name must contain only letters, spaces, hyphens or apostrophes.");
        }

        if (publisher.getCountry() == null || publisher.getCountry().isBlank()) {
            errors.put("country", "Country is required.");
        } else if (!COUNTRY_PATTERN.matcher(publisher.getCountry()).matches()) {
            errors.put("country", "Country must contain only letters, spaces, hyphens or apostrophes.");
        }

        if (publisher.getWebsite() != null && !publisher.getWebsite().isBlank()) {
            if (publisher.getWebsite().length() > 300) {
                errors.put("website", "Website must be 300 characters or less.");
            } else if (!URL_PATTERN.matcher(publisher.getWebsite()).matches()) {
                errors.put("website", "Website must be a valid URL (e.g. https://example.com).");
            }
        }

        if (!errors.isEmpty()) {
            throw new com.library.books.domain.exception.ValidationException(errors);
        }
    }
}
