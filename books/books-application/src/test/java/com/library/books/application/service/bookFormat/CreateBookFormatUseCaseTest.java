package com.library.books.application.service.bookFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.bookFormat.CreateBookFormatCommand;
import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.validation.BookFormatValidator;
import com.library.books.application.service.FakeCodeGenerationService;
import com.library.books.domain.exception.ValidationException;

class CreateBookFormatUseCaseTest {

    @Test
    void createBookFormat_returnsSavedFormat() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        FakeCodeGenerationService codeGen = new FakeCodeGenerationService("FMT");
        CreateBookFormatUseCase useCase = new CreateBookFormatUseCase(bookFormatRepository, validator, codeGen);

        CreateBookFormatCommand command = new CreateBookFormatCommand("Hardback", "Hardcover edition");
        BookFormatResponseDTO result = useCase.execute(command);

        assertEquals("FMT-1", result.code());
        assertEquals("Hardback", result.name());
        assertEquals("Hardcover edition", result.description());
        assertTrue(result.id() > 0);
    }

    @Test
    void createBookFormat_assignsId() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        FakeCodeGenerationService codeGen = new FakeCodeGenerationService("FMT");
        CreateBookFormatUseCase useCase = new CreateBookFormatUseCase(bookFormatRepository, validator, codeGen);

        CreateBookFormatCommand command = new CreateBookFormatCommand("Paperback", null);
        BookFormatResponseDTO result = useCase.execute(command);

        assertTrue(result.id() > 0);
    }

    @Test
    void createBookFormat_failsOnValidationError() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        FakeCodeGenerationService codeGen = new FakeCodeGenerationService("FMT");
        CreateBookFormatUseCase useCase = new CreateBookFormatUseCase(bookFormatRepository, validator, codeGen);

        CreateBookFormatCommand command = new CreateBookFormatCommand("", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertFalse(ex.getFieldErrors().containsKey("code"));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }

    @Test
    void createBookFormat_failsOnDescriptionTooLong() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        FakeCodeGenerationService codeGen = new FakeCodeGenerationService("FMT");
        CreateBookFormatUseCase useCase = new CreateBookFormatUseCase(bookFormatRepository, validator, codeGen);

        String longDescription = "A".repeat(501);
        CreateBookFormatCommand command = new CreateBookFormatCommand("Hardback", longDescription);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("description"));
    }
}
