package com.library.books.application.service.edition;

import com.library.books.application.dto.command.edition.CreateEditionCommand;
import com.library.books.application.dto.response.edition.EditionResponseDTO;
import com.library.books.application.validation.EditionValidator;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;

import com.library.kernel.transaction.Transactional;

public class CreateEditionUseCase {

    private final EditionRepository editionRepository;
    private final EditionValidator editionValidator;
    private final Transactional transactional;

    public CreateEditionUseCase(EditionRepository editionRepository, EditionValidator editionValidator, Transactional transactional) {
        this.editionRepository = editionRepository;
        this.editionValidator = editionValidator;
        this.transactional = transactional;
    }

    public EditionResponseDTO execute(CreateEditionCommand command) {
        if (command.workId() == null) {
            throw new WorkNotFoundException(null);
        }

        Edition edition = Edition.withoutId(
                command.workId(),
                command.publisherId(),
                command.formatId(),
                command.languageId(),
                command.isbn(),
                command.pages(),
                command.publicationYear(),
                command.editionNumber());
        editionValidator.validate(edition);
        Edition saved = editionRepository.save(edition);
        return EditionResponseDTO.of(saved);
    }
}
