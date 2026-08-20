package com.library.books.application.service.language;

import java.util.Map;

import com.library.books.application.dto.command.language.UpdateLanguageCommand;
import com.library.books.application.dto.response.language.LanguageResponseDTO;
import com.library.books.application.validation.LanguageValidator;
import com.library.books.domain.exception.LanguageNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Language;
import com.library.books.domain.port.out.LanguageRepository;

public class UpdateLanguageUseCase {

    private final LanguageRepository languageRepository;
    private final LanguageValidator languageValidator;

    public UpdateLanguageUseCase(LanguageRepository languageRepository, LanguageValidator languageValidator) {
        this.languageRepository = languageRepository;
        this.languageValidator = languageValidator;
    }

    public LanguageResponseDTO execute(UpdateLanguageCommand command) {
        Language existing = languageRepository.findById(command.id())
                .orElseThrow(() -> new LanguageNotFoundException(String.valueOf(command.id())));
        if (languageRepository.findByCode(command.code()).isPresent() 
                && !existing.getCode().equals(command.code())) {
            throw new ValidationException(Map.of("code", "Code already exists"));
        }
        existing.setCode(command.code());
        existing.setName(command.name());
        languageValidator.validate(existing);
        Language saved = languageRepository.save(existing);
        return LanguageResponseDTO.of(saved);
    }
}
