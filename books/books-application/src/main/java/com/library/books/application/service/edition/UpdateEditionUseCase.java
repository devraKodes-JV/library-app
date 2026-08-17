package com.library.books.application.service.edition;

import com.library.books.application.dto.command.edition.UpdateEditionCommand;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.validation.EditionValidator;
import com.library.books.domain.exception.EditionNotFoundException;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;

public class UpdateEditionUseCase {

    private final EditionRepository editionRepository;
    private final EditionValidator editionValidator;

    public UpdateEditionUseCase(EditionRepository editionRepository, EditionValidator editionValidator) {
        this.editionRepository = editionRepository;
        this.editionValidator = editionValidator;
    }

    public EditionResponseDTO execute(UpdateEditionCommand command) {
        Edition existing = editionRepository.findById(command.id())
                .orElseThrow(() -> new EditionNotFoundException(command.id()));
        if (command.workId() == null) {
            throw new WorkNotFoundException(null);
        }

        existing.setWorkId(command.workId());
        existing.setPublisherId(command.publisherId());
        existing.setFormatId(command.formatId());
        existing.setLanguageId(command.languageId());
        existing.setIsbn(command.isbn());
        existing.setPages(command.pages());
        existing.setPublicationYear(command.publicationYear());
        existing.setEditionNumber(command.editionNumber());
        editionValidator.validate(existing);
        Edition saved = editionRepository.save(existing);
        return EditionResponseDTO.of(saved);
    }
}
