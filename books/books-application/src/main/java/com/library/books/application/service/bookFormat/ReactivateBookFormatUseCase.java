package com.library.books.application.service.bookFormat;

import com.library.books.application.dto.command.bookFormat.ReactivateBookFormatCommand;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.domain.port.out.EditionRepository;

public class ReactivateBookFormatUseCase {

    private final BookFormatRepository bookFormatRepository;
    private final EditionRepository editionRepository;

    public ReactivateBookFormatUseCase(BookFormatRepository bookFormatRepository, EditionRepository editionRepository) {
        this.bookFormatRepository = bookFormatRepository;
        this.editionRepository = editionRepository;
    }

    public void execute(ReactivateBookFormatCommand command) {
        bookFormatRepository.reactivateById(command.id());
        bookFormatRepository.reactivateEditionsByFormatId(command.id());
    }
}
