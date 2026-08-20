package com.library.books.application.dto.response.authorRole;

import java.time.Instant;

import com.library.books.domain.model.AuthorRole;

public record AuthorRoleResponseDTO(
        Long id,
        String code,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt) {

    public static AuthorRoleResponseDTO of(AuthorRole authorRole) {
        return new AuthorRoleResponseDTO(
                authorRole.getId(),
                authorRole.getCode(),
                authorRole.getName(),
                authorRole.getDescription(),
                authorRole.getCreatedAt(),
                authorRole.getUpdatedAt());
    }
}
