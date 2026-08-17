package com.library.books.application.dto.common;

import java.time.Instant;

public record BookFormatSummaryDTO(
        Long id,
        String code,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt) {
}
