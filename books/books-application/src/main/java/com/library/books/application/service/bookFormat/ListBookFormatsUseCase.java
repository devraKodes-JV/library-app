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
        return bookFormatRepository.findAll("active").stream()
                .map(BookFormatResponseDTO::of)
                .toList();
    }

    public List<BookFormatResponseDTO> execute(String status) {
        return bookFormatRepository.findAll(status).stream()
                .map(BookFormatResponseDTO::of)
                .toList();
    }
}
