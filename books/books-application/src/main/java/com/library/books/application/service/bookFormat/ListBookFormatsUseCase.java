package com.library.books.application.service.bookFormat;

import com.library.books.domain.model.BookFormat;
import com.library.books.domain.port.out.BookFormatRepository;

import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;

import java.util.List;

public class ListBookFormatsUseCase {

    private final BookFormatRepository bookFormatRepository;

    public ListBookFormatsUseCase(BookFormatRepository bookFormatRepository) {
        this.bookFormatRepository = bookFormatRepository;
    }

    public List<BookFormatResponseDTO> execute() {
        return bookFormatRepository.findAll().stream()
                .map(BookFormatResponseDTO::of)
                .toList();
    }
}
