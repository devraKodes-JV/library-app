package com.library.books.application.service.bookFormat;

import com.library.books.domain.exception.BookFormatNotFoundException;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.port.out.BookFormatRepository;

import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;

public class GetBookFormatUseCase {

    private final BookFormatRepository bookFormatRepository;

    public GetBookFormatUseCase(BookFormatRepository bookFormatRepository) {
        this.bookFormatRepository = bookFormatRepository;
    }

    public BookFormatResponseDTO execute(Long id) {
        BookFormat format = bookFormatRepository.findById(id)
                .orElseThrow(() -> new BookFormatNotFoundException(String.valueOf(id)));
        return BookFormatResponseDTO.of(format);
    }
}
