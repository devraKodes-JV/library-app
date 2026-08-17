package com.library.books.application.dto.response.language;

import java.time.Instant;
import java.util.List;

import com.library.books.domain.dto.common.WorkSummaryDTO;
import com.library.books.domain.model.Language;

public record LanguageDetailResponseDTO(
        Long id,
        String code,
        String name,
        Instant createdAt,
        Instant updatedAt,
        List<WorkSummaryDTO> relatedWorks) {

    public static LanguageDetailResponseDTO from(Language language, List<WorkSummaryDTO> relatedWorks) {
        return new LanguageDetailResponseDTO(
                language.getId(),
                language.getCode(),
                language.getName(),
                language.getCreatedAt(),
                language.getUpdatedAt(),
                relatedWorks != null ? relatedWorks : List.of());
    }
}
