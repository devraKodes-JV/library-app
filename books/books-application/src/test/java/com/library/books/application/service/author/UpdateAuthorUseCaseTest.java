package com.library.books.application.service.author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.author.UpdateAuthorCommand;
import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.validation.AuthorValidator;
import com.library.books.domain.exception.AuthorNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Author;

class UpdateAuthorUseCaseTest {

    @Test
    void updateAuthor_returnsUpdatedAuthor() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        AuthorValidator validator = new AuthorValidator();
        UpdateAuthorUseCase useCase = new UpdateAuthorUseCase(authorRepository, validator);

        Author saved = authorRepository.save(Author.withoutId("John", "Doe", "Old bio", "1980-01-01", null));
        UpdateAuthorCommand command = new UpdateAuthorCommand(saved.getId(), "John", "Smith", "New bio", "1980-01-01", null);
        AuthorResponseDTO result = useCase.execute(command);

        assertEquals("John", result.firstName());
        assertEquals("Smith", result.lastName());
        assertEquals("New bio", result.biography());
    }

    @Test
    void updateAuthor_throwsWhenNotFound() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        AuthorValidator validator = new AuthorValidator();
        UpdateAuthorUseCase useCase = new UpdateAuthorUseCase(authorRepository, validator);

        UpdateAuthorCommand command = new UpdateAuthorCommand(999L, "John", "Doe", null, null, null);

        assertThrows(AuthorNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void updateAuthor_failsOnValidationError() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        AuthorValidator validator = new AuthorValidator();
        UpdateAuthorUseCase useCase = new UpdateAuthorUseCase(authorRepository, validator);

        Author saved = authorRepository.save(Author.withoutId("John", "Doe", null, null, null));
        UpdateAuthorCommand command = new UpdateAuthorCommand(saved.getId(), "", "", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("firstName"));
        assertTrue(ex.getFieldErrors().containsKey("lastName"));
    }

    @Test
    void updateAuthor_failsOnInvalidFirstName() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        AuthorValidator validator = new AuthorValidator();
        UpdateAuthorUseCase useCase = new UpdateAuthorUseCase(authorRepository, validator);

        Author saved = authorRepository.save(Author.withoutId("John", "Doe", null, null, null));
        UpdateAuthorCommand command = new UpdateAuthorCommand(saved.getId(), "John123", "Doe", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("firstName"));
    }
}
