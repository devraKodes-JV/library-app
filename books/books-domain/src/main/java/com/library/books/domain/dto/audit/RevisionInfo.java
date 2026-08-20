package com.library.books.domain.dto.audit;

import java.time.Instant;

public record RevisionInfo(
        long revision,
        Instant timestamp,
        String username) {
}
