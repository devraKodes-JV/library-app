package com.library.books.domain.dto.common;

public record WorkSummaryDTO(
        Long id,
        String title,
        String subtitle,
        String languageName,
        String categoryName) {
}
