package com.library.books.application.service.work;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.work.DeleteWorkCommand;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.Edition;
import com.library.books.domain.model.Work;

class DeleteWorkUseCaseTest {

    @Test
    void deleteWork_removesWork() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeleteWorkUseCase useCase = new DeleteWorkUseCase(workRepository, editionRepository);

        Work saved = workRepository.save(Work.withoutId("The Hobbit", null, 1L, 2L, null));
        useCase.execute(new DeleteWorkCommand(saved.getId()));

        assertTrue(workRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteWork_throwsWhenHasActiveEditions() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeleteWorkUseCase useCase = new DeleteWorkUseCase(workRepository, editionRepository);

        Work saved = workRepository.save(Work.withoutId("The Hobbit", null, 1L, 2L, null));
        Edition edition = Edition.withoutId(saved.getId(), null, null, null, "ISBN", 100, 2020, "1st");
        editionRepository.save(edition);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> useCase.execute(new DeleteWorkCommand(saved.getId())));
        assertEquals("Cannot delete this work because it has active editions. Delete the editions first.",
                ex.getFieldErrors().get("workId"));
    }

    @Test
    void deleteWork_throwsWhenNotFound() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        DeleteWorkUseCase useCase = new DeleteWorkUseCase(workRepository, editionRepository);

        assertThrows(WorkNotFoundException.class,
                () -> useCase.execute(new DeleteWorkCommand(999L)));
    }
}
