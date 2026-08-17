package com.library.books.application.dto.response.publisher;

import java.time.Instant;
import java.util.List;

import com.library.books.domain.dto.common.FlatEditionDTO;
import com.library.books.domain.model.Publisher;

public record PublisherDetailResponseDTO(
        Long id,
        String name,
        String country,
        String website,
        Instant createdAt,
        Instant updatedAt,
        List<FlatEditionDTO> relatedEditions) {

    public static PublisherDetailResponseDTO from(Publisher publisher, List<FlatEditionDTO> relatedEditions) {
        return new PublisherDetailResponseDTO(
                publisher.getId(),
                publisher.getName(),
                publisher.getCountry(),
                publisher.getWebsite(),
                publisher.getCreatedAt(),
                publisher.getUpdatedAt(),
                relatedEditions != null ? relatedEditions : List.of());
    }
}
