package com.library.books.application.dto.response.publisher;

import java.time.Instant;

import com.library.books.domain.model.Publisher;

public record PublisherResponseDTO(
        Long id,
        String name,
        String country,
        String website,
        Instant createdAt,
        Instant updatedAt) {

    public static PublisherResponseDTO of(Publisher publisher) {
        return new PublisherResponseDTO(
                publisher.getId(),
                publisher.getName(),
                publisher.getCountry(),
                publisher.getWebsite(),
                publisher.getCreatedAt(),
                publisher.getUpdatedAt());
    }
}
