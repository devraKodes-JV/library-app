package com.library.books.application.service.edition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.edition.DeleteEditionCommand;
import com.library.books.domain.exception.EditionNotFoundException;
import com.library.books.domain.model.Edition;

class DeleteEditionUseCaseTest {

    @Test
    void deleteEdition_removesEdition() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        DeleteEditionUseCase useCase = new DeleteEditionUseCase(editionRepository, editionAuthorRepository);

        Edition saved = editionRepository.save(Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st"));
        useCase.execute(new DeleteEditionCommand(saved.getId()));

        assertTrue(editionRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteEdition_throwsWhenNotFound() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeEditionAuthorRepository editionAuthorRepository = new FakeEditionAuthorRepository();
        DeleteEditionUseCase useCase = new DeleteEditionUseCase(editionRepository, editionAuthorRepository);

        assertThrows(EditionNotFoundException.class,
                () -> useCase.execute(new DeleteEditionCommand(999L)));
    }
}
