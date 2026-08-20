package com.library.books.application.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Category;

class CategoryValidatorTest {

    private final CategoryValidator validator = new CategoryValidator();

    @Test
    void validate_success_whenValidCategory() {
        Category category = Category.withoutId("FIC", "Fiction", "Fictional works", null);

        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void validate_success_whenDescriptionIsNull() {
        Category category = Category.withoutId("FIC", "Fiction", null, null);

        assertDoesNotThrow(() -> validator.validate(category));
    }

    @Test
    void validate_fails_whenCodeIsNull() {
        Category category = Category.withoutId(null, "Fiction", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        assertEquals("Code is required.", ex.getFieldErrors().get("code"));
    }

    @Test
    void validate_fails_whenCodeIsBlank() {
        Category category = Category.withoutId("   ", "Fiction", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        assertEquals("Code is required.", ex.getFieldErrors().get("code"));
    }

    @Test
    void validate_fails_whenCodeHasInvalidCharacters() {
        Category category = Category.withoutId("FIC-001", "Fiction", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        assertEquals("Code must be alphanumeric and 50 characters or less.", ex.getFieldErrors().get("code"));
    }

    @Test
    void validate_fails_whenCodeIsTooLong() {
        String longCode = "A".repeat(51);
        Category category = Category.withoutId(longCode, "Fiction", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        assertEquals("Code must be alphanumeric and 50 characters or less.", ex.getFieldErrors().get("code"));
    }

    @Test
    void validate_fails_whenNameIsNull() {
        Category category = Category.withoutId("FIC", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        assertEquals("Name is required.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsBlank() {
        Category category = Category.withoutId("FIC", "   ", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        assertEquals("Name is required.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameHasInvalidCharacters() {
        Category category = Category.withoutId("FIC", "Fiction 123", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsTooShort() {
        Category category = Category.withoutId("FIC", "A", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsTooLong() {
        String longName = "A".repeat(101);
        Category category = Category.withoutId("FIC", longName, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenDescriptionIsTooLong() {
        String longDescription = "A".repeat(1001);
        Category category = Category.withoutId("FIC", "Fiction", longDescription, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        assertEquals("Description must be 1000 characters or less.", ex.getFieldErrors().get("description"));
    }

    @Test
    void validate_fails_withMultipleErrors() {
        Category category = Category.withoutId("", "", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(category));
        Map<String, String> errors = ex.getFieldErrors();
        assertEquals(2, errors.size());
        assertEquals("Code is required.", errors.get("code"));
        assertEquals("Name is required.", errors.get("name"));
    }
}
