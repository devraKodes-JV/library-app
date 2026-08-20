package com.library.books.domain.dto.audit;

import java.time.Instant;

public record EntityAuditEntry(
        Long revision,
        Instant revisionTimestamp,
        String username,
        int revisionType,
        Object entity) {
}
