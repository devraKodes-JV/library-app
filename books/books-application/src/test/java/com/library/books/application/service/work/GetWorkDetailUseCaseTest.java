package com.library.books.application.service.work;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.work.WorkDetailResponseDTO;
import com.library.books.domain.dto.common.FlatAuthorDTO;
import com.library.books.domain.dto.query.WorkWithRelationsDTO;
import com.library.books.domain.exception.WorkNotFoundException;

class GetWorkDetailUseCaseTest {

    @Test
    void getWorkDetail_returnsWorkDetail() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        GetWorkDetailUseCase useCase = new GetWorkDetailUseCase(workRepository);

        WorkWithRelationsDTO dto = new WorkWithRelationsDTO(
                1L, "The Hobbit", null, "A great adventure",
                null, null, 1L, "English", 2L, "Fiction",
                 List.of(new FlatAuthorDTO(1L, "John Doe", 1L, "Lead Author"))
        );
        workRepository.putRelations(1L, dto);

        WorkDetailResponseDTO result = useCase.execute(1L);

        assertEquals("The Hobbit", result.title());
        assertEquals("A great adventure", result.summary());
        assertEquals(1, result.authors().size());
        assertEquals("John Doe", result.authors().get(0).displayName());
    }

    @Test
    void getWorkDetail_throwsWhenNotFound() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        GetWorkDetailUseCase useCase = new GetWorkDetailUseCase(workRepository);

        assertThrows(WorkNotFoundException.class, () -> useCase.execute(999L));
    }
}
