package com.library.books.application.service.bookFormat;

import com.library.books.application.dto.command.bookFormat.DeleteBookFormatCommand;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.exception.BookFormatNotFoundException;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.domain.port.out.EditionRepository;

public class DeleteBookFormatUseCase {

    private final BookFormatRepository bookFormatRepository;
    private final EditionRepository editionRepository;

    public DeleteBookFormatUseCase(BookFormatRepository bookFormatRepository, EditionRepository editionRepository) {
        this.bookFormatRepository = bookFormatRepository;
        this.editionRepository = editionRepository;
    }

    public void execute(DeleteBookFormatCommand command) {
        BookFormat existing = bookFormatRepository.findById(command.id())
                .orElseThrow(() -> new BookFormatNotFoundException(String.valueOf(command.id())));

        bookFormatRepository.softDeleteEditionsByFormatId(command.id());
        bookFormatRepository.deleteById(command.id());
    }
}
