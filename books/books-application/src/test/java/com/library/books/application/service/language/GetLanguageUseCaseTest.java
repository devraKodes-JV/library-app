package com.library.books.application.service.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.domain.exception.LanguageNotFoundException;
import com.library.books.domain.model.Language;

class GetLanguageUseCaseTest {

    @Test
    void getLanguage_returnsLanguage() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        GetLanguageUseCase useCase = new GetLanguageUseCase(languageRepository);

        Language saved = languageRepository.save(Language.withoutId("EN", "English"));
        LanguageResponseDTO result = useCase.execute(saved.getId());

        assertEquals("EN", result.code());
        assertEquals("English", result.name());
    }

    @Test
    void getLanguage_throwsWhenNotFound() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        GetLanguageUseCase useCase = new GetLanguageUseCase(languageRepository);

        assertThrows(LanguageNotFoundException.class, () -> useCase.execute(999L));
    }
}
