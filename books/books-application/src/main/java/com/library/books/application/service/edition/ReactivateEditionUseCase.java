package com.library.books.application.service.edition;

import com.library.books.application.dto.command.edition.ReactivateEditionCommand;
import com.library.books.domain.exception.EditionNotFoundException;
import com.library.books.domain.exception.ReactivationException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.BookFormatRepository;
import com.library.books.domain.port.out.EditionAuthorRepository;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.domain.port.out.PublisherRepository;
import com.library.books.domain.port.out.WorkRepository;

public class ReactivateEditionUseCase {

    private final EditionRepository editionRepository;
    private final EditionAuthorRepository editionAuthorRepository;
    private final WorkRepository workRepository;
    private final PublisherRepository publisherRepository;
    private final BookFormatRepository bookFormatRepository;
    private final LanguageRepository languageRepository;

    public ReactivateEditionUseCase(EditionRepository editionRepository,
                                    EditionAuthorRepository editionAuthorRepository,
                                    WorkRepository workRepository,
                                    PublisherRepository publisherRepository,
                                    BookFormatRepository bookFormatRepository,
                                    LanguageRepository languageRepository) {
        this.editionRepository = editionRepository;
        this.editionAuthorRepository = editionAuthorRepository;
        this.workRepository = workRepository;
        this.publisherRepository = publisherRepository;
        this.bookFormatRepository = bookFormatRepository;
        this.languageRepository = languageRepository;
    }

    public void execute(ReactivateEditionCommand command) {
        Edition edition = editionRepository.findByIdIncludingDeleted(command.id())
                .orElseThrow(() -> new EditionNotFoundException(command.id()));

        if (workRepository.findById(edition.getWorkId()).isEmpty()) {
            throw new ReactivationException("Cannot reactivate edition: associated work is not active.");
        }
        if (edition.getPublisherId() != null && publisherRepository.findById(edition.getPublisherId()).isEmpty()) {
            throw new ReactivationException("Cannot reactivate edition: associated publisher is not active.");
        }
        if (edition.getFormatId() != null && bookFormatRepository.findById(edition.getFormatId()).isEmpty()) {
            throw new ReactivationException("Cannot reactivate edition: associated format is not active.");
        }
        if (edition.getLanguageId() != null && languageRepository.findById(edition.getLanguageId()).isEmpty()) {
            throw new ReactivationException("Cannot reactivate edition: associated language is not active.");
        }

        editionRepository.reactivateById(command.id());
        editionAuthorRepository.reactivateByEditionId(command.id());
    }
}
