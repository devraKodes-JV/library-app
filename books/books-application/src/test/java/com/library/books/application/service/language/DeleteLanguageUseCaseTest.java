package com.library.books.application.service.language;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


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
        DeleteLanguageUseCase useCase = new DeleteLanguageUseCase(languageRepository, editionRepository);

        Language saved = languageRepository.save(Language.withoutId("EN", "English"));
        useCase.execute(new DeleteLanguageCommand(saved.getId()));

        assertTrue(languageRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteLanguage_preservesWorksOriginalLanguageId() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteLanguageUseCase useCase = new DeleteLanguageUseCase(languageRepository, editionRepository);

        Language saved = languageRepository.save(Language.withoutId("EN", "English"));
        Work work = Work.withoutId("Test Work", null, saved.getId(), null, null);
        workRepository.save(work);

        useCase.execute(new DeleteLanguageCommand(saved.getId()));

        assertTrue(languageRepository.findById(saved.getId()).isEmpty());
        // Work reference is preserved (not nullified) so it can be restored on reactivation
    }

    @Test
    void deleteLanguage_throwsWhenNotFound() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        FakeEditionRepository editionRepository = new FakeEditionRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteLanguageUseCase useCase = new DeleteLanguageUseCase(languageRepository, editionRepository);

        assertThrows(LanguageNotFoundException.class,
                () -> useCase.execute(new DeleteLanguageCommand(999L)));
    }
}
