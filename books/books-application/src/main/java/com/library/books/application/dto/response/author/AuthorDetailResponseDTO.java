package com.library.books.application.dto.response.author;

import java.time.Instant;
import java.util.List;

import com.library.books.domain.dto.common.WorkSummaryDTO;
import com.library.books.domain.dto.query.AuthorWithWorksDTO;
import com.library.books.domain.model.Author;

public record AuthorDetailResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        String biography,
        String birthDate,
        String deathDate,
        Instant createdAt,
        Instant updatedAt,
        List<WorkSummaryDTO> relatedWorks) {

    public static AuthorDetailResponseDTO from(Author author, List<WorkSummaryDTO> relatedWorks) {
        String fullName = (author.getFirstName() != null ? author.getFirstName() : "")
                + (author.getFirstName() != null && !author.getFirstName().isBlank()
                && author.getLastName() != null && !author.getLastName().isBlank() ? " " : "")
                + (author.getLastName() != null ? author.getLastName() : "");
        return new AuthorDetailResponseDTO(
                author.getId(),
                author.getFirstName(),
                author.getLastName(),
                fullName.trim(),
                author.getBiography(),
                author.getBirthDate(),
                author.getDeathDate(),
                author.getCreatedAt(),
                author.getUpdatedAt(),
                relatedWorks != null ? relatedWorks : List.of());
    }

    public static AuthorDetailResponseDTO from(AuthorWithWorksDTO data) {
        String fullName = (data.firstName() != null ? data.firstName() : "")
                + (data.firstName() != null && !data.firstName().isBlank()
                && data.lastName() != null && !data.lastName().isBlank() ? " " : "")
                + (data.lastName() != null ? data.lastName() : "");
        return new AuthorDetailResponseDTO(
                data.id(),
                data.firstName(),
                data.lastName(),
                fullName.trim(),
                data.biography(),
                data.birthDate(),
                data.deathDate(),
                data.createdAt(),
                data.updatedAt(),
                data.relatedWorks() != null ? data.relatedWorks() : List.of());
    }
}
