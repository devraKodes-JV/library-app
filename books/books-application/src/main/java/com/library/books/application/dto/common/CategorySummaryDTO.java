package com.library.books.application.dto.common;

import java.time.Instant;

public record CategorySummaryDTO(
        Long id,
        String code,
        String name,
        String description,
        Long parentId,
        Instant createdAt,
        Instant updatedAt) {
}
