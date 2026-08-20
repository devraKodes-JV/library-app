package com.library.books.application.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.model.EditionAuthor;

class EditionValidatorTest {

    private final com.library.books.application.validation.EditionValidator validator = new com.library.books.application.validation.EditionValidator();

    @Test
    void validate_success_whenValidEdition() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        assertDoesNotThrow(() -> validator.validate(edition));
    }

    @Test
    void validate_success_whenOptionalFieldsAreNull() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, null, null, null, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        assertDoesNotThrow(() -> validator.validate(edition));
    }

    @Test
    void validate_fails_whenEditionNumberIsNull() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, null, null, null, null);
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(edition));
        assertEquals("Edition number is required.", ex.getFieldErrors().get("editionNumber"));
    }

    @Test
    void validate_fails_whenEditionNumberIsBlank() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, null, null, null, "   ");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(edition));
        assertEquals("Edition number is required.", ex.getFieldErrors().get("editionNumber"));
    }

    @Test
    void validate_fails_whenEditionNumberHasInvalidCharacters() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, null, null, null, "1st!!!");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(edition));
        assertEquals("Edition number must contain only letters, numbers, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("editionNumber"));
    }

    @Test
    void validate_fails_whenIsbnIsInvalid() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "INVALID", 300, 2020, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(edition));
        assertEquals("ISBN must be 10 or 13 digits (optionally ending with X).", ex.getFieldErrors().get("isbn"));
    }

    @Test
    void validate_success_whenIsbnIsValid10Digits() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        assertDoesNotThrow(() -> validator.validate(edition));
    }

    @Test
    void validate_success_whenIsbnIsValid13Digits() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890123", 300, 2020, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        assertDoesNotThrow(() -> validator.validate(edition));
    }

    @Test
    void validate_success_whenIsbnIsValid9DigitsWithX() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "123456789X", 300, 2020, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        assertDoesNotThrow(() -> validator.validate(edition));
    }

    @Test
    void validate_success_whenIsbnHasSpaces() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "123-456-789-0", 300, 2020, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        assertDoesNotThrow(() -> validator.validate(edition));
    }

    @Test
    void validate_fails_whenPagesIsZero() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 0, 2020, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(edition));
        assertEquals("Pages must be a positive number.", ex.getFieldErrors().get("pages"));
    }

    @Test
    void validate_fails_whenPagesIsNegative() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", -10, 2020, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(edition));
        assertEquals("Pages must be a positive number.", ex.getFieldErrors().get("pages"));
    }

    @Test
    void validate_fails_whenPagesIsTooLarge() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 10000, 2020, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(edition));
        assertEquals("Pages must be 9999 or less.", ex.getFieldErrors().get("pages"));
    }

    @Test
    void validate_fails_whenPublicationYearIsTooOld() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 1400, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(edition));
        assertEquals("Publication year must be 1450 or later.", ex.getFieldErrors().get("publicationYear"));
    }

    @Test
    void validate_fails_whenPublicationYearIsTooFarInFuture() {
        int futureYear = java.time.LocalDate.now().getYear() + 6;
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, futureYear, "1st");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(edition));
        assertEquals("Publication year cannot be more than 5 years in the future.", ex.getFieldErrors().get("publicationYear"));
    }

    @Test
    void validate_fails_withMultipleErrors() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "INVALID", 0, 1400, null);
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(edition));
        Map<String, String> errors = ex.getFieldErrors();
        assertEquals(4, errors.size());
        assertEquals("Edition number is required.", errors.get("editionNumber"));
        assertEquals("ISBN must be 10 or 13 digits (optionally ending with X).", errors.get("isbn"));
        assertEquals("Pages must be a positive number.", errors.get("pages"));
        assertEquals("Publication year must be 1450 or later.", errors.get("publicationYear"));
    }

    @Test
    void validate_success_whenEditionNumberHasValidSpecialChars() {
        Edition edition = Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "O'Neill-Smith's 2nd");
        edition.setEditionAuthors(List.of(new EditionAuthor(null, null, 1L, 1L, null, null)));

        assertDoesNotThrow(() -> validator.validate(edition));
    }
}
