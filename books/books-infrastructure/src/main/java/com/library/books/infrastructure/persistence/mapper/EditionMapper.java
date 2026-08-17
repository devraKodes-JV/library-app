package com.library.books.infrastructure.persistence.mapper;

import java.util.List;

import com.library.books.domain.model.Edition;
import com.library.books.domain.model.EditionAuthor;
import com.library.books.infrastructure.persistence.entity.EditionEntity;

public final class EditionMapper {

    private EditionMapper() {
    }

    public static Edition toDomain(EditionEntity e) {
        if (e == null) {
            return null;
        }
        List<EditionAuthor> authors = e.getEditionAuthors() != null ? e.getEditionAuthors().stream()
                .map(EditionAuthorMapper::toDomain)
                .toList()
                : null;
        return new Edition(
                e.getId(),
                e.getWorkId(),
                e.getPublisherId(),
                e.getFormatId(),
                e.getLanguageId(),
                e.getIsbn(),
                e.getPages(),
                e.getPublicationYear(),
                e.getEditionNumber(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                authors);
    }

    public static EditionEntity toEntity(Edition e) {
        if (e == null) {
            return null;
        }
        return new EditionEntity(
                e.getId(),
                e.getWorkId(),
                e.getPublisherId(),
                e.getFormatId(),
                e.getLanguageId(),
                e.getIsbn(),
                e.getPages(),
                e.getPublicationYear(),
                e.getEditionNumber(),
                true);
    }
}
