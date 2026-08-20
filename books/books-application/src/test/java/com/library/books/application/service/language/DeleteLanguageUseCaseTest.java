package com.library.books.application.service.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.language.DeleteLanguageCommand;
import com.library.books.domain.exception.LanguageNotFoundException;
import com.library.books.domain.model.Language;
import com.library.books.domain.model.Work;

class DeleteLanguageUseCaseTest {

    @Test
    void deleteLanguage_removesLanguage() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteLanguageUseCase useCase = new DeleteLanguageUseCase(languageRepository, editionRepository, workRepository);

        Language saved = languageRepository.save(Language.withoutId("EN", "English"));
        useCase.execute(new DeleteLanguageCommand(saved.getId()));

        assertTrue(languageRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteLanguage_nullifiesWorksOriginalLanguageId() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteLanguageUseCase useCase = new DeleteLanguageUseCase(languageRepository, editionRepository, workRepository);

        Language saved = languageRepository.save(Language.withoutId("EN", "English"));
        Work work = Work.withoutId("Test Work", null, saved.getId(), null, null);
        workRepository.save(work);

        useCase.execute(new DeleteLanguageCommand(saved.getId()));

        List<Work> works = workRepository.findByOriginalLanguageId(saved.getId());
        assertTrue(works.isEmpty());

        Work updated = workRepository.findById(work.getId()).orElseThrow();
        assertEquals(null, updated.getOriginalLanguageId());
    }

    @Test
    void deleteLanguage_throwsWhenNotFound() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteLanguageUseCase useCase = new DeleteLanguageUseCase(languageRepository, editionRepository, workRepository);

        assertThrows(LanguageNotFoundException.class,
                () -> useCase.execute(new DeleteLanguageCommand(999L)));
    }
}
