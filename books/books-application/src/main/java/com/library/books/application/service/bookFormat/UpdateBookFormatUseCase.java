package com.library.books.application.service.bookFormat;

import java.util.Map;

import com.library.books.application.dto.command.bookFormat.UpdateBookFormatCommand;
import com.library.books.application.dto.response.booksFormat.BookFormatResponseDTO;
import com.library.books.application.validation.BookFormatValidator;
import com.library.books.domain.exception.BookFormatNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.port.out.BookFormatRepository;

public class UpdateBookFormatUseCase {

    private final BookFormatRepository bookFormatRepository;
    private final BookFormatValidator bookFormatValidator;

    public UpdateBookFormatUseCase(BookFormatRepository bookFormatRepository, BookFormatValidator bookFormatValidator) {
        this.bookFormatRepository = bookFormatRepository;
        this.bookFormatValidator = bookFormatValidator;
    }

    public BookFormatResponseDTO execute(UpdateBookFormatCommand command) {
        BookFormat existing = bookFormatRepository.findById(command.id())
                .orElseThrow(() -> new BookFormatNotFoundException(String.valueOf(command.id())));
        if (bookFormatRepository.findByCode(command.code()).isPresent() 
                && !existing.getCode().equals(command.code())) {
            throw new ValidationException(Map.of("code", "Code already exists"));
        }
        existing.setCode(command.code());
        existing.setName(command.name());
        existing.setDescription(command.description());
        bookFormatValidator.validate(existing);
        BookFormat saved = bookFormatRepository.save(existing);
        return BookFormatResponseDTO.of(saved);
    }
}
