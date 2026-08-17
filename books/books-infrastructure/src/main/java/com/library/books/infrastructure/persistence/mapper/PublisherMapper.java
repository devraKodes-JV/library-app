package com.library.books.infrastructure.persistence.mapper;

import com.library.books.domain.model.Publisher;
import com.library.books.infrastructure.persistence.entity.PublisherEntity;

public final class PublisherMapper {

    private PublisherMapper() {
    }

    public static Publisher toDomain(PublisherEntity e) {
        if (e == null) {
            return null;
        }
        return new Publisher(
                e.getId(),
                e.getName(),
                e.getCountry(),
                e.getWebsite(),
                e.getCreatedAt(),
                e.getUpdatedAt());
    }

    public static PublisherEntity toEntity(Publisher p) {
        if (p == null) {
            return null;
        }
        return new PublisherEntity(
                p.getId(),
                p.getName(),
                p.getCountry(),
                p.getWebsite(),
                true);
    }
}
