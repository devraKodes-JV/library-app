package com.library.books.application.dto.response.booksFormat;

import java.time.Instant;

import com.library.books.domain.model.BookFormat;

public record BookFormatResponseDTO(
        Long id,
        String code,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt) {

    public static BookFormatResponseDTO of(BookFormat format) {
        return new BookFormatResponseDTO(
                format.getId(),
                format.getCode(),
                format.getName(),
                format.getDescription(),
                format.getCreatedAt(),
                format.getUpdatedAt());
    }
}
