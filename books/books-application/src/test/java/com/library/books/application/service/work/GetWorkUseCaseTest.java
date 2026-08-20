package com.library.books.application.service.work;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.domain.dto.query.WorkWithRelationsDTO;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.Work;

class GetWorkUseCaseTest {

    @Test
    void getWork_returnsWork() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        GetWorkUseCase useCase = new GetWorkUseCase(workRepository);

        Work saved = workRepository.save(Work.withoutId("The Hobbit", null, 1L, 2L, "A great adventure"));
        workRepository.putRelations(saved.getId(), new WorkWithRelationsDTO(
                saved.getId(), "The Hobbit", null, "A great adventure",
                null, null, 1L, "English", 2L, "Fiction", List.of()
        ));

        WorkResponseDTO result = useCase.execute(saved.getId());

        assertEquals("The Hobbit", result.title());
        assertEquals("A great adventure", result.summary());
    }

    @Test
    void getWork_throwsWhenNotFound() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        GetWorkUseCase useCase = new GetWorkUseCase(workRepository);

        assertThrows(WorkNotFoundException.class, () -> useCase.execute(999L));
    }
}
