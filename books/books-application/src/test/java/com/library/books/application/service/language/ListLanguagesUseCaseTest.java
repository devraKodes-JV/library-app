package com.library.books.application.service.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.domain.model.Language;

class ListLanguagesUseCaseTest {

    @Test
    void listLanguages_returnsAll() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        ListLanguagesUseCase useCase = new ListLanguagesUseCase(languageRepository);

        languageRepository.save(Language.withoutId("EN", "English"));
        languageRepository.save(Language.withoutId("ES", "Spanish"));

        List<LanguageResponseDTO> result = useCase.execute();

        assertEquals(2, result.size());
        assertEquals("EN", result.get(0).code());
        assertEquals("ES", result.get(1).code());
    }

    @Test
    void listLanguages_returnsEmptyWhenNone() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        ListLanguagesUseCase useCase = new ListLanguagesUseCase(languageRepository);

        List<LanguageResponseDTO> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
