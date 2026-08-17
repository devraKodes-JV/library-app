package com.library.books.application.dto.response.author;

import java.time.Instant;

import com.library.books.domain.model.Author;

public record AuthorResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        String biography,
        String birthDate,
        String deathDate,
        Instant createdAt,
        Instant updatedAt) {

    public static AuthorResponseDTO of(Author author) {
        String fullName = (author.getFirstName() != null ? author.getFirstName() : "")
                + (author.getFirstName() != null && !author.getFirstName().isBlank()
                        && author.getLastName() != null && !author.getLastName().isBlank() ? " " : "")
                + (author.getLastName() != null ? author.getLastName() : "");
        return new AuthorResponseDTO(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                fullName.trim(),
                author.getBiography(),
                author.getBirthDate(),
                author.getDeathDate(),
                author.getCreatedAt(),
                author.getUpdatedAt());
    }
}
