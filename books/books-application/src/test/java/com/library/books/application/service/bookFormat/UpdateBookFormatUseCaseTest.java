package com.library.books.application.service.bookFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.bookFormat.UpdateBookFormatCommand;
import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.validation.BookFormatValidator;
import com.library.books.domain.exception.BookFormatNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.BookFormat;

class UpdateBookFormatUseCaseTest {

    @Test
    void updateBookFormat_returnsUpdatedFormat() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        UpdateBookFormatUseCase useCase = new UpdateBookFormatUseCase(bookFormatRepository, validator);

        BookFormat saved = bookFormatRepository.save(BookFormat.withoutId("HARDBACK", "Hardback", "Old desc"));
        UpdateBookFormatCommand command = new UpdateBookFormatCommand(saved.getId(), "HARDBACK", "Hardback Updated", "New desc");
        BookFormatResponseDTO result = useCase.execute(command);

        assertEquals("HARDBACK", result.code());
        assertEquals("Hardback Updated", result.name());
        assertEquals("New desc", result.description());
    }

    @Test
    void updateBookFormat_throwsWhenNotFound() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        UpdateBookFormatUseCase useCase = new UpdateBookFormatUseCase(bookFormatRepository, validator);

        UpdateBookFormatCommand command = new UpdateBookFormatCommand(999L, "HARDBACK", "Hardback", null);

        assertThrows(BookFormatNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void updateBookFormat_failsOnValidationError() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        UpdateBookFormatUseCase useCase = new UpdateBookFormatUseCase(bookFormatRepository, validator);

        BookFormat saved = bookFormatRepository.save(BookFormat.withoutId("HARDBACK", "Hardback", null));
        UpdateBookFormatCommand command = new UpdateBookFormatCommand(saved.getId(), "", "", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }

    @Test
    void updateBookFormat_failsOnInvalidCode() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        BookFormatValidator validator = new BookFormatValidator();
        UpdateBookFormatUseCase useCase = new UpdateBookFormatUseCase(bookFormatRepository, validator);

        BookFormat saved = bookFormatRepository.save(BookFormat.withoutId("HARDBACK", "Hardback", null));
        UpdateBookFormatCommand command = new UpdateBookFormatCommand(saved.getId(), "INVALID-CODE", "Hardback", null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
    }
}
