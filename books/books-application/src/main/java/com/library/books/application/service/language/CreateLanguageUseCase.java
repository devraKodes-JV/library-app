package com.library.books.application.service.language;

import com.library.books.application.dto.command.language.CreateLanguageCommand;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.validation.LanguageValidator;
import com.library.books.domain.model.Language;
import com.library.books.domain.port.out.LanguageRepository;

public class CreateLanguageUseCase {

    private final LanguageRepository languageRepository;
    private final LanguageValidator languageValidator;

    public CreateLanguageUseCase(LanguageRepository languageRepository, LanguageValidator languageValidator) {
        this.languageRepository = languageRepository;
        this.languageValidator = languageValidator;
    }

    public LanguageResponseDTO execute(CreateLanguageCommand command) {
        Language language = Language.withoutId(command.code(), command.name());
        languageValidator.validate(language);
        Language saved = languageRepository.save(language);
        return LanguageResponseDTO.of(saved);
    }
}
