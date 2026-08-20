package com.library.books.application.service.bookFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.bookFormat.DeleteBookFormatCommand;
import com.library.books.domain.exception.BookFormatNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.model.Edition;

class DeleteBookFormatUseCaseTest {

    @Test
    void deleteBookFormat_removesFormat() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeleteBookFormatUseCase useCase = new DeleteBookFormatUseCase(bookFormatRepository, editionRepository);

        BookFormat saved = bookFormatRepository.save(BookFormat.withoutId("HARDBACK", "Hardback", "Hardcover edition"));
        useCase.execute(new DeleteBookFormatCommand(saved.getId()));

        assertTrue(bookFormatRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteBookFormat_throwsWhenHasActiveEditions() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeleteBookFormatUseCase useCase = new DeleteBookFormatUseCase(bookFormatRepository, editionRepository);

        BookFormat saved = bookFormatRepository.save(BookFormat.withoutId("HARDBACK", "Hardback", "Hardcover edition"));
        Edition edition = Edition.withoutId(null, null, saved.getId(), null, "ISBN", 100, 2020, "1st");
        editionRepository.save(edition);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> useCase.execute(new DeleteBookFormatCommand(saved.getId())));
        assertEquals("Cannot delete this format because it has active editions. Delete the editions first.",
                ex.getFieldErrors().get("formatId"));
    }

    @Test
    void deleteBookFormat_throwsWhenNotFound() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeleteBookFormatUseCase useCase = new DeleteBookFormatUseCase(bookFormatRepository, editionRepository);

        assertThrows(BookFormatNotFoundException.class,
                () -> useCase.execute(new DeleteBookFormatCommand(999L)));
    }
}
