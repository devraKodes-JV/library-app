package com.library.books.domain.exception;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Domain exception thrown when input validation fails.
 *
 * <p>It lives in the domain layer because validation is a business rule,
 * not a web concern. The application layer throws it inside use cases,
 * and the web layer maps it to form error rendering.</p>
 */
public class ValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.fieldErrors = new java.util.LinkedHashMap<>();
    }

    public ValidationException() {
        super("Validation failed");
        this.fieldErrors = new java.util.LinkedHashMap<>();
    }

    public ValidationException(Map<String, String> fieldErrors) {
        super("Validation failed");
        this.fieldErrors = new LinkedHashMap<>(fieldErrors);
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void addFieldError(String field, String error) {
        this.fieldErrors.put(field, error);
    }
}
