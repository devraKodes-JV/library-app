package com.library.books.application.dto.response.edition;

import java.time.Instant;

import com.library.books.domain.model.Edition;

public record EditionResponseDTO(
        Long id,
        Long workId,
        String workTitle,
        Long publisherId,
        String publisherName,
        Long formatId,
        String formatName,
        Long languageId,
        String languageName,
        String isbn,
        Integer pages,
        Integer publicationYear,
        String editionNumber,
        Instant createdAt,
        Instant updatedAt) {

    public static EditionResponseDTO of(Edition edition, String workTitle, String publisherName,
                                String formatName, String languageName) {
        return new EditionResponseDTO(
                edition.getId(),
                edition.getWorkId(),
                workTitle != null && !workTitle.isBlank() ? workTitle : "",
                edition.getPublisherId(),
                publisherName != null && !publisherName.isBlank() ? publisherName : "",
                edition.getFormatId(),
                formatName != null && !formatName.isBlank() ? formatName : "",
                edition.getLanguageId(),
                languageName != null && !languageName.isBlank() ? languageName : "",
                edition.getIsbn(),
                edition.getPages(),
                edition.getPublicationYear(),
                edition.getEditionNumber(),
                edition.getCreatedAt(),
                edition.getUpdatedAt());
    }

    public static EditionResponseDTO of(Edition edition) {
        return new EditionResponseDTO(
                edition.getId(),
                edition.getWorkId(),
                null,
                edition.getPublisherId(),
                null,
                edition.getFormatId(),
                null,
                edition.getLanguageId(),
                null,
                edition.getIsbn(),
                edition.getPages(),
                edition.getPublicationYear(),
                edition.getEditionNumber(),
                edition.getCreatedAt(),
                edition.getUpdatedAt());
    }
}
