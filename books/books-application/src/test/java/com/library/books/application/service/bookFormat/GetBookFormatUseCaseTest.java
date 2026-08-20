package com.library.books.application.service.bookFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.domain.exception.BookFormatNotFoundException;
import com.library.books.domain.model.BookFormat;

class GetBookFormatUseCaseTest {

    @Test
    void getBookFormat_returnsFormat() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        GetBookFormatUseCase useCase = new GetBookFormatUseCase(bookFormatRepository);

        BookFormat saved = bookFormatRepository.save(BookFormat.withoutId("HARDBACK", "Hardback", "Hardcover edition"));
        BookFormatResponseDTO result = useCase.execute(saved.getId());

        assertEquals("HARDBACK", result.code());
        assertEquals("Hardback", result.name());
        assertEquals("Hardcover edition", result.description());
    }

    @Test
    void getBookFormat_throwsWhenNotFound() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        GetBookFormatUseCase useCase = new GetBookFormatUseCase(bookFormatRepository);

        assertThrows(BookFormatNotFoundException.class, () -> useCase.execute(999L));
    }
}
