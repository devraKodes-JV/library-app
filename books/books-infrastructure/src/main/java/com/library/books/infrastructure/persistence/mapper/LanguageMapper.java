package com.library.books.infrastructure.persistence.mapper;

import com.library.books.domain.model.Language;
import com.library.books.infrastructure.persistence.entity.LanguageEntity;

public final class LanguageMapper {

    private LanguageMapper() {
    }

    public static Language toDomain(LanguageEntity e) {
        if (e == null) {
            return null;
        }
        return new Language(
                e.getId(),
                e.getCode(),
                e.getName(),
                e.getCreatedAt(),
                e.getUpdatedAt());
    }

    public static LanguageEntity toEntity(Language l) {
        if (l == null) {
            return null;
        }
        return new LanguageEntity(
                l.getId(),
                l.getCode(),
                l.getName(),
                true);
    }
}
