package com.library.books.application.service.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.language.UpdateLanguageCommand;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.validation.LanguageValidator;
import com.library.books.domain.exception.LanguageNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Language;

class UpdateLanguageUseCaseTest {

    @Test
    void updateLanguage_returnsUpdatedLanguage() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        LanguageValidator validator = new LanguageValidator();
        UpdateLanguageUseCase useCase = new UpdateLanguageUseCase(languageRepository, validator);

        Language saved = languageRepository.save(Language.withoutId("EN", "English"));
        UpdateLanguageCommand command = new UpdateLanguageCommand(saved.getId(), "EN", "English Updated");
        LanguageResponseDTO result = useCase.execute(command);

        assertEquals("EN", result.code());
        assertEquals("English Updated", result.name());
    }

    @Test
    void updateLanguage_throwsWhenNotFound() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        LanguageValidator validator = new LanguageValidator();
        UpdateLanguageUseCase useCase = new UpdateLanguageUseCase(languageRepository, validator);

        UpdateLanguageCommand command = new UpdateLanguageCommand(999L, "EN", "English");

        assertThrows(LanguageNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void updateLanguage_failsOnValidationError() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        LanguageValidator validator = new LanguageValidator();
        UpdateLanguageUseCase useCase = new UpdateLanguageUseCase(languageRepository, validator);

        Language saved = languageRepository.save(Language.withoutId("EN", "English"));
        UpdateLanguageCommand command = new UpdateLanguageCommand(saved.getId(), "", "");

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }

    @Test
    void updateLanguage_failsOnInvalidCode() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        LanguageValidator validator = new LanguageValidator();
        UpdateLanguageUseCase useCase = new UpdateLanguageUseCase(languageRepository, validator);

        Language saved = languageRepository.save(Language.withoutId("EN", "English"));
        UpdateLanguageCommand command = new UpdateLanguageCommand(saved.getId(), "INVALID-CODE", "English");

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
    }
}
