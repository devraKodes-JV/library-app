package com.library.books.infrastructure.persistence.mapper;

import com.library.books.domain.model.EditionAuthor;
import com.library.books.infrastructure.persistence.entity.EditionAuthorEntity;

public final class EditionAuthorMapper {

    private EditionAuthorMapper() {
    }

    public static EditionAuthor toDomain(EditionAuthorEntity entity) {
        if (entity == null) {
            return null;
        }
        return new EditionAuthor(
                entity.getId(),
                entity.getEditionId(),
                entity.getAuthorId(),
                entity.getAuthorRoleId(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static EditionAuthorEntity toEntity(EditionAuthor domain) {
        if (domain == null) {
            return null;
        }
        EditionAuthorEntity entity = new EditionAuthorEntity();
        entity.setId(domain.getId());
        entity.setEditionId(domain.getEditionId());
        entity.setAuthorId(domain.getAuthorId());
        entity.setAuthorRoleId(domain.getAuthorRoleId());
        entity.setEnabled(true);
        return entity;
    }
}
