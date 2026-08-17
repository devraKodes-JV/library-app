package com.library.books.domain.dto.query;

import java.time.Instant;
import java.util.List;

import com.library.books.domain.dto.common.FlatAuthorDTO;

public record WorkWithRelationsDTO(
        Long id,
        String title,
        String subtitle,
        String summary,
        Instant createdAt,
        Instant updatedAt,
        Long originalLanguageId,
        String languageName,
        Long categoryId,
        String categoryName,
        List<FlatAuthorDTO> authors) {
}
