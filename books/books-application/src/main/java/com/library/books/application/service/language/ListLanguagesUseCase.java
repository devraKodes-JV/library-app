package com.library.books.application.service.language;

import com.library.books.domain.model.Language;
import com.library.books.domain.port.out.LanguageRepository;

import com.library.books.application.dto.response.language.LanguageResponseDTO;

import java.util.List;

public class ListLanguagesUseCase {

    private final LanguageRepository languageRepository;

    public ListLanguagesUseCase(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public List<LanguageResponseDTO> execute() {
        return languageRepository.findAll().stream()
                .map(LanguageResponseDTO::of)
                .toList();
    }
}
