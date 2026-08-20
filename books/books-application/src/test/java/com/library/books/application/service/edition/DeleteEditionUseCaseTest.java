package com.library.books.application.service.edition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.edition.DeleteEditionCommand;
import com.library.books.domain.model.Edition;

class DeleteEditionUseCaseTest {

    @Test
    void deleteEdition_removesEdition() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeleteEditionUseCase useCase = new DeleteEditionUseCase(editionRepository);

        Edition saved = editionRepository.save(Edition.withoutId(1L, 2L, 3L, 4L, "1234567890", 300, 2020, "1st"));
        useCase.execute(new DeleteEditionCommand(saved.getId()));

        assertTrue(editionRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteEdition_doesNotThrowWhenNotFound() {
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeleteEditionUseCase useCase = new DeleteEditionUseCase(editionRepository);

        assertDoesNotThrow(() -> useCase.execute(new DeleteEditionCommand(999L)));
    }
}
