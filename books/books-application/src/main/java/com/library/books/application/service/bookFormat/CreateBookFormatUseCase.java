package com.library.books.application.service.bookFormat;

import java.util.Map;

import com.library.books.application.dto.command.bookFormat.CreateBookFormatCommand;
import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.validation.BookFormatValidator;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.port.out.BookFormatRepository;

public class CreateBookFormatUseCase {

    private final BookFormatRepository bookFormatRepository;
    private final BookFormatValidator bookFormatValidator;

    public CreateBookFormatUseCase(BookFormatRepository bookFormatRepository, BookFormatValidator bookFormatValidator) {
        this.bookFormatRepository = bookFormatRepository;
        this.bookFormatValidator = bookFormatValidator;
    }

    public BookFormatResponseDTO execute(CreateBookFormatCommand command) {
        if (bookFormatRepository.findByCode(command.code()).isPresent()) {
            throw new ValidationException(Map.of("code", "Code already exists"));
        }
        BookFormat format = BookFormat.withoutId(command.code(), command.name(), command.description());
        bookFormatValidator.validate(format);
        BookFormat saved = bookFormatRepository.save(format);
        return BookFormatResponseDTO.of(saved);
    }
}
