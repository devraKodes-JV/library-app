package com.library.books.application.service.work;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.domain.model.Work;

class ListWorksByLanguageUseCaseTest {

    @Test
    void listWorksByLanguage_returnsWorksForLanguage() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        ListWorksByLanguageUseCase useCase = new ListWorksByLanguageUseCase(workRepository, languageRepository, categoryRepository);

        Work work = Work.withoutId("The Hobbit", null, 1L, 2L, null);
        workRepository.save(work);

        List<WorkResponseDTO> result = useCase.execute(1L);

        assertEquals(1, result.size());
        assertEquals("The Hobbit", result.get(0).title());
    }

    @Test
    void listWorksByLanguage_returnsEmptyWhenNoWorks() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        ListWorksByLanguageUseCase useCase = new ListWorksByLanguageUseCase(workRepository, languageRepository, categoryRepository);

        List<WorkResponseDTO> result = useCase.execute(1L);

        assertTrue(result.isEmpty());
    }
}
