package com.library.books.application.validation;

import com.library.books.domain.model.Work;
import com.library.kernel.validation.Validator;

public class WorkValidator implements Validator<Work> {

    private static final java.util.regex.Pattern TITLE_PATTERN = java.util.regex.Pattern.compile("^[a-zA-ZÀ-ÿ0-9\\s'-]{1,200}$");
    private static final java.util.regex.Pattern SUBTITLE_PATTERN = java.util.regex.Pattern.compile("^[a-zA-ZÀ-ÿ0-9\\s'-]*$");

    @Override
    public void validate(Work work) {
        var errors = new java.util.LinkedHashMap<String, String>();

        if (work.getTitle() == null || work.getTitle().isBlank()) {
            errors.put("title", "Title is required.");
        } else if (!TITLE_PATTERN.matcher(work.getTitle()).matches()) {
            errors.put("title", "Title must contain only letters, numbers, spaces, hyphens or apostrophes.");
        }

        if (work.getSubtitle() != null && work.getSubtitle().length() > 500) {
            errors.put("subtitle", "Subtitle must be 500 characters or less.");
        } else if (work.getSubtitle() != null && !SUBTITLE_PATTERN.matcher(work.getSubtitle()).matches()) {
            errors.put("subtitle", "Subtitle must contain only letters, numbers, hyphens or apostrophes.");
        }

        if (work.getSummary() != null && work.getSummary().length() > 2000) {
            errors.put("summary", "Summary must be 2000 characters or less.");
        }

        if (work.getWorkAuthors() == null || work.getWorkAuthors().isEmpty()) {
            errors.put("authors", "At least one author is required");
        }

        if (!errors.isEmpty()) {
            throw new com.library.books.domain.exception.ValidationException(errors);
        }
    }
}
