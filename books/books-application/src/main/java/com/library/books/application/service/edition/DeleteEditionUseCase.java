package com.library.books.application.service.edition;

import com.library.books.application.dto.command.edition.DeleteEditionCommand;
import com.library.books.domain.exception.EditionNotFoundException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionAuthorRepository;
import com.library.books.domain.port.out.EditionRepository;

public class DeleteEditionUseCase {

    private final EditionRepository editionRepository;
    private final EditionAuthorRepository editionAuthorRepository;

    public DeleteEditionUseCase(EditionRepository editionRepository, EditionAuthorRepository editionAuthorRepository) {
        this.editionRepository = editionRepository;
        this.editionAuthorRepository = editionAuthorRepository;
    }

    public void execute(DeleteEditionCommand command) {
        Edition existing = editionRepository.findById(command.id())
                .orElseThrow(() -> new EditionNotFoundException(command.id()));
        editionAuthorRepository.softDeleteByEditionId(command.id());
        editionRepository.deleteById(command.id());
    }
}
