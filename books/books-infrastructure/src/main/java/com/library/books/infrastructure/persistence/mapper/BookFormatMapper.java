package com.library.books.infrastructure.persistence.mapper;

import com.library.books.domain.model.BookFormat;
import com.library.books.infrastructure.persistence.entity.BookFormatEntity;

public final class BookFormatMapper {

    private BookFormatMapper() {
    }

    public static BookFormat toDomain(BookFormatEntity e) {
        if (e == null) {
            return null;
        }
        return new BookFormat(
                e.getId(),
                e.getCode(),
                e.getName(),
                e.getDescription(),
                e.getCreatedAt(),
                e.getUpdatedAt());
    }

    public static BookFormatEntity toEntity(BookFormat f) {
        if (f == null) {
            return null;
        }
        return new BookFormatEntity(
                f.getId(),
                f.getCode(),
                f.getName(),
                f.getDescription(),
                true);
    }
}
