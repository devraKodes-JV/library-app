package com.library.books.application.service.language;

import com.library.books.domain.exception.LanguageNotFoundException;
import com.library.books.domain.model.Language;
import com.library.books.domain.port.out.LanguageRepository;

import com.library.books.application.dto.response.language.LanguageResponseDTO;

public class GetLanguageUseCase {

    private final LanguageRepository languageRepository;

    public GetLanguageUseCase(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public LanguageResponseDTO execute(Long id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new LanguageNotFoundException(String.valueOf(id)));
        return LanguageResponseDTO.of(language);
    }
}
