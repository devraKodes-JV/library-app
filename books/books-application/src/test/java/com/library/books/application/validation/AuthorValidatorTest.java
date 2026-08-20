package com.library.books.application.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Author;

class AuthorValidatorTest {

    private final AuthorValidator validator = new AuthorValidator();

    @Test
    void validate_success_whenValidAuthor() {
        Author author = Author.withoutId("John", "Doe", "Famous writer", "1980-01-01", null);

        assertDoesNotThrow(() -> validator.validate(author));
    }

    @Test
    void validate_success_whenAllFieldsProvided() {
        Author author = Author.withoutId("Jane", "Doe", "Writer", "1990-05-15", "2020-12-01");

        assertDoesNotThrow(() -> validator.validate(author));
    }

    @Test
    void validate_fails_whenFirstNameIsNull() {
        Author author = Author.withoutId(null, "Doe", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("First name is required.", ex.getFieldErrors().get("firstName"));
    }

    @Test
    void validate_fails_whenFirstNameIsBlank() {
        Author author = Author.withoutId("   ", "Doe", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("First name is required.", ex.getFieldErrors().get("firstName"));
    }

    @Test
    void validate_fails_whenFirstNameHasInvalidCharacters() {
        Author author = Author.withoutId("John123", "Doe", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("First name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("firstName"));
    }

    @Test
    void validate_fails_whenFirstNameIsTooShort() {
        Author author = Author.withoutId("J", "Doe", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("First name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("firstName"));
    }

    @Test
    void validate_fails_whenFirstNameIsTooLong() {
        String longName = "A".repeat(101);
        Author author = Author.withoutId(longName, "Doe", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("First name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("firstName"));
    }

    @Test
    void validate_fails_whenLastNameIsNull() {
        Author author = Author.withoutId("John", null, null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Last name is required.", ex.getFieldErrors().get("lastName"));
    }

    @Test
    void validate_fails_whenLastNameIsBlank() {
        Author author = Author.withoutId("John", "   ", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Last name is required.", ex.getFieldErrors().get("lastName"));
    }

    @Test
    void validate_fails_whenLastNameHasInvalidCharacters() {
        Author author = Author.withoutId("John", "Doe123", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Last name must contain only letters, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("lastName"));
    }

    @Test
    void validate_fails_whenBiographyIsTooLong() {
        String longBio = "A".repeat(2001);
        Author author = Author.withoutId("John", "Doe", longBio, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Biography must be 2000 characters or less.", ex.getFieldErrors().get("biography"));
    }

    @Test
    void validate_fails_whenBiographyHasInvalidCharacters() {
        Author author = Author.withoutId("John", "Doe", "Invalid@bio", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Biography contains invalid characters.", ex.getFieldErrors().get("biography"));
    }

    @Test
    void validate_fails_whenBirthDateIsInFuture() {
        String futureDate = java.time.LocalDate.now().plusDays(1).toString();
        Author author = Author.withoutId("John", "Doe", null, futureDate, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertTrue(ex.getFieldErrors().containsKey("birthDate"));
    }

    @Test
    void validate_fails_whenBirthDateIsTooRecent() {
        String recentDate = java.time.LocalDate.now().minusYears(10).toString();
        Author author = Author.withoutId("John", "Doe", null, recentDate, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Author must be at least 18 years old.", ex.getFieldErrors().get("birthDate"));
    }

    @Test
    void validate_fails_whenBirthDateIsInvalidFormat() {
        Author author = Author.withoutId("John", "Doe", null, "invalid-date", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Invalid birth date format. Use YYYY-MM-DD.", ex.getFieldErrors().get("birthDate"));
    }

    @Test
    void validate_fails_whenDeathDateIsInFuture() {
        String futureDate = java.time.LocalDate.now().plusDays(1).toString();
        Author author = Author.withoutId("John", "Doe", null, "1980-01-01", futureDate);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Death date cannot be in the future.", ex.getFieldErrors().get("deathDate"));
    }

    @Test
    void validate_fails_whenDeathDateIsBeforeBirthDate() {
        Author author = Author.withoutId("John", "Doe", null, "1990-01-01", "1980-01-01");

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Death date cannot be before birth date.", ex.getFieldErrors().get("deathDate"));
    }

    @Test
    void validate_fails_whenDeathDateIsInvalidFormat() {
        Author author = Author.withoutId("John", "Doe", null, "1980-01-01", "invalid-date");

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Invalid death date format. Use YYYY-MM-DD.", ex.getFieldErrors().get("deathDate"));
    }

    @Test
    void validate_fails_withMultipleErrors() {
        Author author = Author.withoutId("", "", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        Map<String, String> errors = ex.getFieldErrors();
        assertEquals(2, errors.size());
        assertEquals("First name is required.", errors.get("firstName"));
        assertEquals("Last name is required.", errors.get("lastName"));
    }

    @Test
    void validate_success_whenNameHasValidSpecialChars() {
        Author author = Author.withoutId("O'Neill-Smith", "D'Artagnan", null, null, null);

        assertDoesNotThrow(() -> validator.validate(author));
    }

    @Test
    void validate_fails_whenBiographyHasInvalidChars() {
        Author author = Author.withoutId("John", "Doe", "Contains@invalid#chars", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(author));
        assertEquals("Biography contains invalid characters.", ex.getFieldErrors().get("biography"));
    }

    @Test
    void validate_success_whenBiographyHasValidSpecialChars() {
        Author author = Author.withoutId("John", "Doe", "Biography with 123, !?\"'-.", null, null);

        assertDoesNotThrow(() -> validator.validate(author));
    }
}
