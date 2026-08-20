package com.library.books.application.service.bookFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.bookFormat.CreateBookFormatCommand;
import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.validation.BookFormatValidator;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.BookFormat;

class CreateBookFormatUseCaseTest {

    @Test
    void createBookFormat_returnsSavedFormat() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        CreateBookFormatUseCase useCase = new CreateBookFormatUseCase(bookFormatRepository, validator);

        CreateBookFormatCommand command = new CreateBookFormatCommand("HARDBACK", "Hardback", "Hardcover edition");
        BookFormatResponseDTO result = useCase.execute(command);

        assertEquals("HARDBACK", result.code());
        assertEquals("Hardback", result.name());
        assertEquals("Hardcover edition", result.description());
        assertTrue(result.id() > 0);
    }

    @Test
    void createBookFormat_assignsId() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        CreateBookFormatUseCase useCase = new CreateBookFormatUseCase(bookFormatRepository, validator);

        CreateBookFormatCommand command = new CreateBookFormatCommand("PAPERBACK", "Paperback", null);
        BookFormatResponseDTO result = useCase.execute(command);

        assertTrue(result.id() > 0);
    }

    @Test
    void createBookFormat_failsOnValidationError() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        CreateBookFormatUseCase useCase = new CreateBookFormatUseCase(bookFormatRepository, validator);

        CreateBookFormatCommand command = new CreateBookFormatCommand("", "", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }

    @Test
    void createBookFormat_failsOnInvalidCode() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        CreateBookFormatUseCase useCase = new CreateBookFormatUseCase(bookFormatRepository, validator);

        CreateBookFormatCommand command = new CreateBookFormatCommand("HARD_BACK", "Hardback", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
        assertEquals("Code must be alphanumeric and 20 characters or less.", ex.getFieldErrors().get("code"));
    }

    @Test
    void createBookFormat_failsOnDescriptionTooLong() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        CreateBookFormatUseCase useCase = new CreateBookFormatUseCase(bookFormatRepository, validator);

        String longDescription = "A".repeat(501);
        CreateBookFormatCommand command = new CreateBookFormatCommand("HARDBACK", "Hardback", longDescription);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("description"));
    }
}
