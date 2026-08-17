package com.library.books.application.dto.response.category;

import java.time.Instant;

import com.library.books.domain.model.Category;

public record CategoryResponseDTO(
        Long id,
        String code,
        String name,
        String description,
        Long parentId,
        String parentName,
        Instant createdAt,
        Instant updatedAt) {

    public static CategoryResponseDTO of(Category category) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getCode(),
                category.getName(),
                category.getDescription(),
                category.getParentId(),
                null,
                category.getCreatedAt(),
                category.getUpdatedAt());
    }

    public static CategoryResponseDTO of(Category category, String parentName) {
        return new CategoryResponseDTO(
                category.getId(),
                category.getCode(),
                category.getName(),
                category.getDescription(),
                category.getParentId(),
                parentName != null && !parentName.isBlank() ? parentName : "",
                category.getCreatedAt(),
                category.getUpdatedAt());
    }
}
