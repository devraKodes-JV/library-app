package com.library.books.application.service.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.language.CreateLanguageCommand;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.validation.LanguageValidator;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Language;

class CreateLanguageUseCaseTest {

    @Test
    void createLanguage_returnsSavedLanguage() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        LanguageValidator validator = new LanguageValidator();
        CreateLanguageUseCase useCase = new CreateLanguageUseCase(languageRepository, validator);

        CreateLanguageCommand command = new CreateLanguageCommand("EN", "English");
        LanguageResponseDTO result = useCase.execute(command);

        assertEquals("EN", result.code());
        assertEquals("English", result.name());
        assertTrue(result.id() > 0);
    }

    @Test
    void createLanguage_assignsId() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        LanguageValidator validator = new LanguageValidator();
        CreateLanguageUseCase useCase = new CreateLanguageUseCase(languageRepository, validator);

        CreateLanguageCommand command = new CreateLanguageCommand("ES", "Spanish");
        LanguageResponseDTO result = useCase.execute(command);

        assertTrue(result.id() > 0);
    }

    @Test
    void createLanguage_failsOnValidationError() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        LanguageValidator validator = new LanguageValidator();
        CreateLanguageUseCase useCase = new CreateLanguageUseCase(languageRepository, validator);

        CreateLanguageCommand command = new CreateLanguageCommand("", "");

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }

    @Test
    void createLanguage_failsOnInvalidCode() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        LanguageValidator validator = new LanguageValidator();
        CreateLanguageUseCase useCase = new CreateLanguageUseCase(languageRepository, validator);

        languageRepository.save(Language.withoutId("EN", "English"));
        CreateLanguageCommand command = new CreateLanguageCommand("EN-US", "English");

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
        assertEquals("Code must be alphanumeric and 20 characters or less.", ex.getFieldErrors().get("code"));
    }
}
