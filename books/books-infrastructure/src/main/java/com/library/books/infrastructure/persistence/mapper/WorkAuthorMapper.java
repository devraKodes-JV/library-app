package com.library.books.infrastructure.persistence.mapper;

import com.library.books.domain.model.WorkAuthor;
import com.library.books.infrastructure.persistence.entity.WorkAuthorEntity;

public class WorkAuthorMapper {

    public static WorkAuthor toDomain(WorkAuthorEntity entity) {
        if (entity == null) {
            return null;
        }
        return new WorkAuthor(
                entity.getId(),
                entity.getWorkId(),
                entity.getAuthorId(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static WorkAuthorEntity toEntity(WorkAuthor domain) {
        if (domain == null) {
            return null;
        }
        WorkAuthorEntity entity = new WorkAuthorEntity();
        entity.setId(domain.getId());
        entity.setWorkId(domain.getWorkId());
        entity.setAuthorId(domain.getAuthorId());
        entity.setRole(domain.getRole());
        entity.setEnabled(true);
        return entity;
    }
}
