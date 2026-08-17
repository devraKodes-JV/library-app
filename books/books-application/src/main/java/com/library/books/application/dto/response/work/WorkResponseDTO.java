package com.library.books.application.dto.response.work;

import java.time.Instant;
import java.util.List;

import com.library.books.domain.model.Work;
import com.library.books.domain.model.WorkAuthor;

public record WorkResponseDTO(
        Long id,
        String title,
        String subtitle,
        Long originalLanguageId,
        String originalLanguageName,
        Long categoryId,
        String categoryName,
        String summary,
        Instant createdAt,
        Instant updatedAt,
        List<WorkAuthor> authors) {

    public static WorkResponseDTO of(Work work, String languageName, String categoryName, List<WorkAuthor> authors) {
        return new WorkResponseDTO(
                work.getId(),
                work.getTitle(),
                work.getSubtitle(),
                work.getOriginalLanguageId(),
                languageName != null && !languageName.isBlank() ? languageName : "",
                work.getCategoryId(),
                categoryName != null && !categoryName.isBlank() ? categoryName : "",
                work.getSummary(),
                work.getCreatedAt(),
                work.getUpdatedAt(),
                authors != null ? authors : List.of());
    }

    public static WorkResponseDTO of(Long id, String title, String subtitle, String languageName, String categoryName, List<WorkAuthor> authors) {
        return new WorkResponseDTO(
                id,
                title,
                subtitle,
                null,
                languageName != null && !languageName.isBlank() ? languageName : "",
                null,
                categoryName != null && !categoryName.isBlank() ? categoryName : "",
                null,
                null,
                null,
                authors != null ? authors : List.of());
    }

    public static WorkResponseDTO of(Work work, String languageName, String categoryName) {
        return of(work, languageName, categoryName, List.of());
    }
}
