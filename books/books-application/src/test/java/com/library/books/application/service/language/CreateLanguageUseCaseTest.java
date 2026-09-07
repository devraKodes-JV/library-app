package com.library.books.application.service.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.language.CreateLanguageCommand;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.validation.LanguageValidator;
import com.library.books.application.service.FakeCodeGenerationService;
import com.library.books.domain.exception.ValidationException;

class CreateLanguageUseCaseTest {

    @Test
    void createLanguage_returnsSavedLanguage() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        LanguageValidator validator = new LanguageValidator();
        FakeCodeGenerationService codeGen = new FakeCodeGenerationService("LANG");
        CreateLanguageUseCase useCase = new CreateLanguageUseCase(languageRepository, validator, codeGen);

        CreateLanguageCommand command = new CreateLanguageCommand("English");
        LanguageResponseDTO result = useCase.execute(command);

        assertEquals("LANG-1", result.code());
        assertEquals("English", result.name());
        assertEquals(1L, result.id());
    }

    @Test
    void createLanguage_failsOnValidationError() {
        FakeLanguageRepository languageRepository = new FakeLanguageRepository();
        LanguageValidator validator = new LanguageValidator();
        FakeCodeGenerationService codeGen = new FakeCodeGenerationService("LANG");
        CreateLanguageUseCase useCase = new CreateLanguageUseCase(languageRepository, validator, codeGen);

        CreateLanguageCommand command = new CreateLanguageCommand("");

        assertThrows(ValidationException.class, () -> useCase.execute(command));
    }
}
