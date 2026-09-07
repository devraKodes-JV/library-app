package com.library.books.application.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Language;

class LanguageValidatorTest {

    private final LanguageValidator validator = new LanguageValidator();

    @Test
    void validate_success_whenValidLanguage() {
        Language language = Language.withoutId("EN", "English");

        assertDoesNotThrow(() -> validator.validate(language));
    }

    @Test
    void validate_success_whenCodeIsSingleChar() {
        Language language = Language.withoutId("E", "English");

        assertDoesNotThrow(() -> validator.validate(language));
    }

    @Test
    void validate_fails_whenCodeIsNull() {
        Language language = Language.withoutId(null, "English");

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(language));
        assertEquals("Code is required.", ex.getFieldErrors().get("code"));
    }

    @Test
    void validate_fails_whenCodeIsBlank() {
        Language language = Language.withoutId("   ", "English");

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(language));
        assertEquals("Code is required.", ex.getFieldErrors().get("code"));
    }

    @Test
    void validate_success_whenCodeHasHyphen() {
        Language language = Language.withoutId("LANG-a1b2c3D4", "English");

        validator.validate(language);
    }

    @Test
    void validate_fails_whenNameIsNull() {
        Language language = Language.withoutId("EN", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(language));
        assertEquals("Name is required.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsBlank() {
        Language language = Language.withoutId("EN", "   ");

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(language));
        assertEquals("Name is required.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameHasInvalidCharacters() {
        Language language = Language.withoutId("EN", "English 123");

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(language));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsTooShort() {
        Language language = Language.withoutId("EN", "A");

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(language));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsTooLong() {
        String longName = "A".repeat(101);
        Language language = Language.withoutId("EN", longName);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(language));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_withMultipleErrors() {
        Language language = Language.withoutId("", "");

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(language));
        Map<String, String> errors = ex.getFieldErrors();
        assertEquals(2, errors.size());
        assertEquals("Code is required.", errors.get("code"));
        assertEquals("Name is required.", errors.get("name"));
    }

    @Test
    void validate_success_whenNameHasValidSpecialChars() {
        Language language = Language.withoutId("EN", "O'Neill-Smith");

        assertDoesNotThrow(() -> validator.validate(language));
    }
}
