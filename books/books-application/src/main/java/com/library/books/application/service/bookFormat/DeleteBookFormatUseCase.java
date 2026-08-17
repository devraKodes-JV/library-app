package com.library.books.application.service.bookFormat;

import com.library.books.application.dto.command.bookFormat.DeleteBookFormatCommand;
import com.library.books.domain.exception.BookFormatNotFoundException;
import com.library.books.domain.model.BookFormat;
import com.library.books.domain.port.out.BookFormatRepository;

public class DeleteBookFormatUseCase {

    private final BookFormatRepository bookFormatRepository;

    public DeleteBookFormatUseCase(BookFormatRepository bookFormatRepository) {
        this.bookFormatRepository = bookFormatRepository;
    }

    public void execute(DeleteBookFormatCommand command) {
        BookFormat existing = bookFormatRepository.findById(command.id())
                .orElseThrow(() -> new BookFormatNotFoundException(String.valueOf(command.id())));
        bookFormatRepository.deleteById(command.id());
    }
}
