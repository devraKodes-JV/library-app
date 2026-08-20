package com.library.books.infrastructure.persistence.mapper;

import com.library.books.domain.model.AuthorRole;
import com.library.books.infrastructure.persistence.entity.AuthorRoleEntity;

public class AuthorRoleMapper {

    public static AuthorRole toDomain(AuthorRoleEntity entity) {
        if (entity == null) {
            return null;
        }
        return new AuthorRole(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static AuthorRoleEntity toEntity(AuthorRole domain) {
        if (domain == null) {
            return null;
        }
        AuthorRoleEntity entity = new AuthorRoleEntity();
        entity.setId(domain.getId());
        entity.setCode(domain.getCode());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        return entity;
    }
}
