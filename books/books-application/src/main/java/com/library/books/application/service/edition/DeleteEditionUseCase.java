package com.library.books.application.service.edition;

import com.library.books.application.dto.command.edition.DeleteEditionCommand;
import com.library.books.domain.exception.EditionNotFoundException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;

public class DeleteEditionUseCase {

    private final EditionRepository editionRepository;

    public DeleteEditionUseCase(EditionRepository editionRepository) {
        this.editionRepository = editionRepository;
    }

    public void execute(DeleteEditionCommand command) {
        editionRepository.deleteById(command.id());
    }
}
