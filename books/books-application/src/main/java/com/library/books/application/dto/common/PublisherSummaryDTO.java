package com.library.books.application.dto.common;

import java.time.Instant;

public record PublisherSummaryDTO(
        Long id,
        String name,
        String country,
        String website,
        Instant createdAt,
        Instant updatedAt) {
}
