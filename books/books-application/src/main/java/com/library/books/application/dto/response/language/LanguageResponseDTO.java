package com.library.books.application.dto.response.language;

import java.time.Instant;

import com.library.books.domain.model.Language;

public record LanguageResponseDTO(
        Long id,
        String code,
        String name,
        Instant createdAt,
        Instant updatedAt) {

    public static LanguageResponseDTO of(Language language) {
        return new LanguageResponseDTO(
                language.getId(),
                language.getCode(),
                language.getName(),
                language.getCreatedAt(),
                language.getUpdatedAt());
    }
}
