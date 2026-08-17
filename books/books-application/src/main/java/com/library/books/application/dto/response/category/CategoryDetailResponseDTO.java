package com.library.books.application.dto.response.category;

import java.time.Instant;
import java.util.List;

import com.library.books.domain.dto.common.WorkSummaryDTO;
import com.library.books.domain.model.Category;

public record CategoryDetailResponseDTO(
        Long id,
        String code,
        String name,
        String description,
        Long parentId,
        String parentName,
        Instant createdAt,
        Instant updatedAt,
        List<WorkSummaryDTO> relatedWorks) {

    public static CategoryDetailResponseDTO from(Category category, String parentName, List<WorkSummaryDTO> relatedWorks) {
        return new CategoryDetailResponseDTO(
                category.getId(),
                category.getCode(),
                category.getName(),
                category.getDescription(),
                category.getParentId(),
                parentName != null && !parentName.isBlank() ? parentName : "",
                category.getCreatedAt(),
                category.getUpdatedAt(),
                relatedWorks != null ? relatedWorks : List.of());
    }
}
