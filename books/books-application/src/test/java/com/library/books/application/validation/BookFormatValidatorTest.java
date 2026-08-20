package com.library.books.application.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.BookFormat;

class BookFormatValidatorTest {

    private final BookFormatValidator validator = new BookFormatValidator();

    @Test
    void validate_success_whenValidFormat() {
        BookFormat format = BookFormat.withoutId("HARDBACK", "Hardback", "Hardcover edition");

        assertDoesNotThrow(() -> validator.validate(format));
    }

    @Test
    void validate_success_whenDescriptionIsNull() {
        BookFormat format = BookFormat.withoutId("HARDBACK", "Hardback", null);

        assertDoesNotThrow(() -> validator.validate(format));
    }

    @Test
    void validate_success_whenDescriptionIsEmpty() {
        BookFormat format = BookFormat.withoutId("HARDBACK", "Hardback", "");

        assertDoesNotThrow(() -> validator.validate(format));
    }

    @Test
    void validate_fails_whenCodeIsNull() {
        BookFormat format = BookFormat.withoutId(null, "Hardback", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        assertEquals("Code is required.", ex.getFieldErrors().get("code"));
    }

    @Test
    void validate_fails_whenCodeIsBlank() {
        BookFormat format = BookFormat.withoutId("   ", "Hardback", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        assertEquals("Code is required.", ex.getFieldErrors().get("code"));
    }

    @Test
    void validate_fails_whenCodeHasInvalidCharacters() {
        BookFormat format = BookFormat.withoutId("HARD_BACK", "Hardback", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        assertEquals("Code must be alphanumeric and 20 characters or less.", ex.getFieldErrors().get("code"));
    }

    @Test
    void validate_fails_whenCodeIsTooLong() {
        String longCode = "A".repeat(21);
        BookFormat format = BookFormat.withoutId(longCode, "Hardback", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        assertEquals("Code must be alphanumeric and 20 characters or less.", ex.getFieldErrors().get("code"));
    }

    @Test
    void validate_fails_whenNameIsNull() {
        BookFormat format = BookFormat.withoutId("HARDBACK", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        assertEquals("Name is required.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsBlank() {
        BookFormat format = BookFormat.withoutId("HARDBACK", "   ", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        assertEquals("Name is required.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameHasInvalidCharacters() {
        BookFormat format = BookFormat.withoutId("HARDBACK", "Hardback123", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsTooShort() {
        BookFormat format = BookFormat.withoutId("HARDBACK", "A", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsTooLong() {
        String longName = "A".repeat(101);
        BookFormat format = BookFormat.withoutId("HARDBACK", longName, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenDescriptionIsTooLong() {
        String longDescription = "A".repeat(501);
        BookFormat format = BookFormat.withoutId("HARDBACK", "Hardback", longDescription);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        assertEquals("Description must be 500 characters or less.", ex.getFieldErrors().get("description"));
    }

    @Test
    void validate_fails_withMultipleErrors() {
        BookFormat format = BookFormat.withoutId("", "", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(format));
        Map<String, String> errors = ex.getFieldErrors();
        assertEquals(2, errors.size());
        assertEquals("Code is required.", errors.get("code"));
        assertEquals("Name is required.", errors.get("name"));
    }

    @Test
    void validate_success_whenNameHasValidSpecialChars() {
        BookFormat format = BookFormat.withoutId("PAPERBACK", "O'Reilly-Scott", "Paperback edition");

        assertDoesNotThrow(() -> validator.validate(format));
    }
}
