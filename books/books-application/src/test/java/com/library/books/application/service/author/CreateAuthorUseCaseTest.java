package com.library.books.application.service.author;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.author.CreateAuthorCommand;
import com.library.books.application.dto.response.author.AuthorResponseDTO;
import com.library.books.application.validation.AuthorValidator;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Author;

class CreateAuthorUseCaseTest {

    @Test
    void createAuthor_returnsSavedAuthor() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        AuthorValidator validator = new AuthorValidator();
        CreateAuthorUseCase useCase = new CreateAuthorUseCase(authorRepository, validator);

        CreateAuthorCommand command = new CreateAuthorCommand("John", "Doe", "Famous writer", "1980-01-01", null);
        AuthorResponseDTO result = useCase.execute(command);

        assertEquals("John", result.firstName());
        assertEquals("Doe", result.lastName());
        assertEquals("John Doe", result.fullName());
        assertTrue(result.id() > 0);
    }

    @Test
    void createAuthor_assignsId() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        AuthorValidator validator = new AuthorValidator();
        CreateAuthorUseCase useCase = new CreateAuthorUseCase(authorRepository, validator);

        CreateAuthorCommand command = new CreateAuthorCommand("Jane", "Smith", null, null, null);
        AuthorResponseDTO result = useCase.execute(command);

        assertTrue(result.id() > 0);
    }

    @Test
    void createAuthor_failsOnValidationError() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        AuthorValidator validator = new AuthorValidator();
        CreateAuthorUseCase useCase = new CreateAuthorUseCase(authorRepository, validator);

        CreateAuthorCommand command = new CreateAuthorCommand("", "", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("firstName"));
        assertTrue(ex.getFieldErrors().containsKey("lastName"));
    }

    @Test
    void createAuthor_failsOnInvalidFirstName() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        AuthorValidator validator = new AuthorValidator();
        CreateAuthorUseCase useCase = new CreateAuthorUseCase(authorRepository, validator);

        CreateAuthorCommand command = new CreateAuthorCommand("John123", "Doe", null, null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("firstName"));
    }

    @Test
    void createAuthor_failsOnBirthDateInFuture() {
        FakeAuthorRepository authorRepository = new FakeAuthorRepository();
        AuthorValidator validator = new AuthorValidator();
        CreateAuthorUseCase useCase = new CreateAuthorUseCase(authorRepository, validator);

        String futureDate = java.time.LocalDate.now().plusDays(1).toString();
        CreateAuthorCommand command = new CreateAuthorCommand("John", "Doe", null, futureDate, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("birthDate"));
    }
}
