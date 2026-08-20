package com.library.books.application.service.bookFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.domain.model.BookFormat;

class ListBookFormatsUseCaseTest {

    @Test
    void listBookFormats_returnsAll() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        ListBookFormatsUseCase useCase = new ListBookFormatsUseCase(bookFormatRepository);

        bookFormatRepository.save(BookFormat.withoutId("HARDBACK", "Hardback", "Hardcover edition"));
        bookFormatRepository.save(BookFormat.withoutId("PAPERBACK", "Paperback", "Softcover edition"));

        List<BookFormatResponseDTO> result = useCase.execute();

        assertEquals(2, result.size());
        assertEquals("HARDBACK", result.get(0).code());
        assertEquals("PAPERBACK", result.get(1).code());
    }

    @Test
    void listBookFormats_returnsEmptyWhenNone() {
        FakeBookFormatRepository bookFormatRepository = new FakeBookFormatRepository();
        ListBookFormatsUseCase useCase = new ListBookFormatsUseCase(bookFormatRepository);

        List<BookFormatResponseDTO> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
