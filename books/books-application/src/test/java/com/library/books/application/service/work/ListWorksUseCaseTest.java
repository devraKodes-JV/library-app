package com.library.books.application.service.work;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.domain.model.Work;
import com.library.kernel.web.Page;

class ListWorksUseCaseTest {

    @Test
    void listWorks_returnsPagedWorks() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        ListWorksUseCase useCase = new ListWorksUseCase(workRepository, languageRepository, categoryRepository);

        Work work1 = Work.withoutId("The Hobbit", null, 1L, 2L, "A great adventure");
        workRepository.save(work1);
        Work work2 = Work.withoutId("The Lord of the Rings", null, 1L, 2L, "An epic tale");
        workRepository.save(work2);

        Page<WorkResponseDTO> result = useCase.execute(0, 10);

        assertEquals(2, result.totalElements());
        assertEquals(1, result.totalPages());
        assertEquals(2, result.items().size());
        assertEquals("The Hobbit", result.items().get(0).title());
    }

    @Test
    void listWorks_returnsEmptyWhenNone() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        ListWorksUseCase useCase = new ListWorksUseCase(workRepository, languageRepository, categoryRepository);

        Page<WorkResponseDTO> result = useCase.execute(0, 10);

        assertTrue(result.items().isEmpty());
        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());
    }

    @Test
    void listWorks_respectsPageSize() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        ListWorksUseCase useCase = new ListWorksUseCase(workRepository, languageRepository, categoryRepository);

        for (int i = 0; i < 5; i++) {
            Work work = Work.withoutId("Work " + i, null, 1L, 2L, null);
            workRepository.save(work);
        }

        Page<WorkResponseDTO> result = useCase.execute(0, 2);

        assertEquals(5, result.totalElements());
        assertEquals(3, result.totalPages());
        assertEquals(2, result.items().size());
    }
}
