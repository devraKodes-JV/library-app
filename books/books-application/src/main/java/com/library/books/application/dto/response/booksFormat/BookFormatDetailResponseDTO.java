package com.library.books.application.dto.response.booksFormat;

import java.time.Instant;
import java.util.List;

import com.library.books.domain.dto.common.FlatEditionDTO;
import com.library.books.domain.model.BookFormat;

public record BookFormatDetailResponseDTO(
        Long id,
        String code,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt,
        List<FlatEditionDTO> relatedEditions) {

    public static BookFormatDetailResponseDTO from(BookFormat format, List<FlatEditionDTO> relatedEditions) {
        return new BookFormatDetailResponseDTO(
                format.getId(),
                format.getCode(),
                format.getName(),
                format.getDescription(),
                format.getCreatedAt(),
                format.getUpdatedAt(),
                relatedEditions != null ? relatedEditions : List.of());
    }
}
