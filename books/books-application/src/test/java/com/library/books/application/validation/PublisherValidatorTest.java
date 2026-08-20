package com.library.books.application.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Publisher;

class PublisherValidatorTest {

    private final PublisherValidator validator = new PublisherValidator();

    @Test
    void validate_success_whenValidPublisher() {
        Publisher publisher = Publisher.withoutId("Penguin", "UK", "https://penguin.com");

        assertDoesNotThrow(() -> validator.validate(publisher));
    }

    @Test
    void validate_success_whenWebsiteIsNull() {
        Publisher publisher = Publisher.withoutId("Penguin", "UK", null);

        assertDoesNotThrow(() -> validator.validate(publisher));
    }

    @Test
    void validate_success_whenWebsiteIsBlank() {
        Publisher publisher = Publisher.withoutId("Penguin", "UK", "   ");

        assertDoesNotThrow(() -> validator.validate(publisher));
    }

    @Test
    void validate_fails_whenNameIsNull() {
        Publisher publisher = Publisher.withoutId(null, "UK", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        assertEquals("Name is required.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsBlank() {
        Publisher publisher = Publisher.withoutId("   ", "UK", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        assertEquals("Name is required.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameHasInvalidCharacters() {
        Publisher publisher = Publisher.withoutId("Penguin123", "UK", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsTooShort() {
        Publisher publisher = Publisher.withoutId("A", "UK", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenNameIsTooLong() {
        String longName = "A".repeat(101);
        Publisher publisher = Publisher.withoutId(longName, "UK", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        assertEquals("Name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("name"));
    }

    @Test
    void validate_fails_whenCountryIsNull() {
        Publisher publisher = Publisher.withoutId("Penguin", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        assertEquals("Country is required.", ex.getFieldErrors().get("country"));
    }

    @Test
    void validate_fails_whenCountryIsBlank() {
        Publisher publisher = Publisher.withoutId("Penguin", "   ", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        assertEquals("Country is required.", ex.getFieldErrors().get("country"));
    }

    @Test
    void validate_fails_whenCountryHasInvalidCharacters() {
        Publisher publisher = Publisher.withoutId("Penguin", "UK123", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        assertEquals("Country must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("country"));
    }

    @Test
    void validate_fails_whenWebsiteIsTooLong() {
        String longWebsite = "https://" + "a".repeat(290) + ".com";
        Publisher publisher = Publisher.withoutId("Penguin", "UK", longWebsite);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        assertEquals("Website must be 300 characters or less.", ex.getFieldErrors().get("website"));
    }

    @Test
    void validate_fails_whenWebsiteIsInvalid() {
        Publisher publisher = Publisher.withoutId("Penguin", "UK", "not-a-url");

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        assertEquals("Website must be a valid URL (e.g. https://example.com).", ex.getFieldErrors().get("website"));
    }

    @Test
    void validate_fails_withMultipleErrors() {
        Publisher publisher = Publisher.withoutId("", "", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(publisher));
        Map<String, String> errors = ex.getFieldErrors();
        assertEquals(2, errors.size());
        assertEquals("Name is required.", errors.get("name"));
        assertEquals("Country is required.", errors.get("country"));
    }

    @Test
    void validate_success_whenNameHasValidSpecialChars() {
        Publisher publisher = Publisher.withoutId("O'Reilly-Scott", "USA", "https://oreilly.com");

        assertDoesNotThrow(() -> validator.validate(publisher));
    }
}
