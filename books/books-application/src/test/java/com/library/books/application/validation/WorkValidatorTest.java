package com.library.books.application.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Work;
import com.library.books.domain.model.WorkAuthor;

class WorkValidatorTest {

    private final com.library.books.application.validation.WorkValidator validator = new com.library.books.application.validation.WorkValidator();

    @Test
    void validate_success_whenValidWork() {
        Work work = Work.withoutId("The Hobbit", null, 1L, 2L, "A great adventure");
        work.setWorkAuthors(List.of(new WorkAuthor(null, null, 1L, 1L, null, null)));

        assertDoesNotThrow(() -> validator.validate(work));
    }

    @Test
    void validate_success_whenSubtitleAndSummaryAreNull() {
        Work work = Work.withoutId("The Hobbit", null, 1L, 2L, null);
        work.setWorkAuthors(List.of(new WorkAuthor(null, null, 1L, 1L, null, null)));

        assertDoesNotThrow(() -> validator.validate(work));
    }

    @Test
    void validate_fails_whenTitleIsNull() {
        Work work = Work.withoutId(null, null, 1L, 2L, null);
        work.setWorkAuthors(List.of(new WorkAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(work));
        assertEquals("Title is required.", ex.getFieldErrors().get("title"));
    }

    @Test
    void validate_fails_whenTitleIsBlank() {
        Work work = Work.withoutId("   ", null, 1L, 2L, null);
        work.setWorkAuthors(List.of(new WorkAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(work));
        assertEquals("Title is required.", ex.getFieldErrors().get("title"));
    }

    @Test
    void validate_fails_whenTitleHasInvalidCharacters() {
        Work work = Work.withoutId("The Hobbit!!!", null, 1L, 2L, null);
        work.setWorkAuthors(List.of(new WorkAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(work));
        assertEquals("Title must contain only letters, numbers, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("title"));
    }

    @Test
    void validate_fails_whenTitleIsTooLong() {
        String longTitle = "A".repeat(201);
        Work work = Work.withoutId(longTitle, null, 1L, 2L, null);
        work.setWorkAuthors(List.of(new WorkAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(work));
        assertEquals("Title must contain only letters, numbers, spaces, hyphens or apostrophes.", ex.getFieldErrors().get("title"));
    }

    @Test
    void validate_fails_whenSubtitleIsTooLong() {
        String longSubtitle = "A".repeat(501);
        Work work = Work.withoutId("The Hobbit", longSubtitle, 1L, 2L, null);
        work.setWorkAuthors(List.of(new WorkAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(work));
        assertEquals("Subtitle must be 500 characters or less.", ex.getFieldErrors().get("subtitle"));
    }

    @Test
    void validate_fails_whenSubtitleHasInvalidCharacters() {
        Work work = Work.withoutId("The Hobbit", "Invalid!!!", 1L, 2L, null);
        work.setWorkAuthors(List.of(new WorkAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(work));
        assertEquals("Subtitle must contain only letters, numbers, hyphens or apostrophes.", ex.getFieldErrors().get("subtitle"));
    }

    @Test
    void validate_fails_whenSummaryIsTooLong() {
        String longSummary = "A".repeat(2001);
        Work work = Work.withoutId("The Hobbit", null, 1L, 2L, longSummary);
        work.setWorkAuthors(List.of(new WorkAuthor(null, null, 1L, 1L, null, null)));

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(work));
        assertEquals("Summary must be 2000 characters or less.", ex.getFieldErrors().get("summary"));
    }

    @Test
    void validate_fails_whenNoAuthors() {
        Work work = Work.withoutId("The Hobbit", null, 1L, 2L, null);
        work.setWorkAuthors(List.of());

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(work));
        assertEquals("At least one author is required", ex.getFieldErrors().get("authors"));
    }

    @Test
    void validate_fails_whenAuthorsIsNull() {
        Work work = Work.withoutId("The Hobbit", null, 1L, 2L, null);
        work.setWorkAuthors(null);

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(work));
        assertEquals("At least one author is required", ex.getFieldErrors().get("authors"));
    }

    @Test
    void validate_fails_withMultipleErrors() {
        Work work = Work.withoutId("", null, 1L, 2L, null);
        work.setWorkAuthors(List.of());

        ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(work));
        Map<String, String> errors = ex.getFieldErrors();
        assertEquals(2, errors.size());
        assertEquals("Title is required.", errors.get("title"));
        assertEquals("At least one author is required", errors.get("authors"));
    }

    @Test
    void validate_success_whenTitleHasValidSpecialChars() {
        Work work = Work.withoutId("O'Neill-Smith's Adventure", null, 1L, 2L, null);
        work.setWorkAuthors(List.of(new WorkAuthor(null, null, 1L, 1L, null, null)));

        assertDoesNotThrow(() -> validator.validate(work));
    }
}
