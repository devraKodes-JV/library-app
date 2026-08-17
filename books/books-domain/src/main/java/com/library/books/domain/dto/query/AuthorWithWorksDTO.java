package com.library.books.domain.dto.query;

import java.time.Instant;
import java.util.List;

import com.library.books.domain.dto.common.WorkSummaryDTO;

public record AuthorWithWorksDTO(
        Long id,
        String firstName,
        String lastName,
        String biography,
        String birthDate,
        String deathDate,
        Instant createdAt,
        Instant updatedAt,
        List<WorkSummaryDTO> relatedWorks) {
}
