package com.library.books.application.dto.common;

import java.time.Instant;

public record LanguageSummaryDTO(
        Long id,
        String code,
        String name,
        Instant createdAt,
        Instant updatedAt) {
}
