package com.library.books.infrastructure.persistence.mapper;

import com.library.books.domain.model.Author;
import com.library.books.infrastructure.persistence.entity.AuthorEntity;

public final class AuthorMapper {

    private AuthorMapper() {
    }

    public static Author toDomain(AuthorEntity e) {
        if (e == null) {
            return null;
        }
        return new Author(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getBiography(),
                e.getBirthDate(),
                e.getDeathDate(),
                e.getCreatedAt(),
                e.getUpdatedAt());
    }

    public static AuthorEntity toEntity(Author a) {
        if (a == null) {
            return null;
        }
        return new AuthorEntity(
                a.getId(),
                a.getFirstName(),
                a.getLastName(),
                a.getBiography(),
                a.getBirthDate(),
                a.getDeathDate(),
                true);
    }
}
