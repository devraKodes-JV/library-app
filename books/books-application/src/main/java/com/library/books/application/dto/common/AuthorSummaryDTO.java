package com.library.books.application.dto.common;

import java.time.Instant;

public record AuthorSummaryDTO(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        Instant createdAt,
        Instant updatedAt) {
}
