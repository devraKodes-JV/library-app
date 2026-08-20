package com.library.books.application.dto.response.edition;

import java.time.Instant;
import java.util.List;

import com.library.books.domain.model.Edition;
import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.dto.response.edition.EditionWithNamesDTO;

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
        Instant updatedAt,
        List<EditionAuthor> editionAuthors) {

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
                edition.getUpdatedAt(),
                edition.getEditionAuthors());
    }

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
                edition.getUpdatedAt(),
                List.of());
    }

    public static EditionResponseDTO of(Edition edition, String workTitle, String publisherName,
                                String formatName, String languageName, List<EditionAuthor> editionAuthors) {
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
                edition.getUpdatedAt(),
                editionAuthors);
    }

    public static EditionResponseDTO from(EditionWithNamesDTO dto) {
        return new EditionResponseDTO(
                dto.id(),
                dto.workId(),
                dto.workTitle() != null && !dto.workTitle().isBlank() ? dto.workTitle() : "",
                dto.publisherId(),
                dto.publisherName() != null && !dto.publisherName().isBlank() ? dto.publisherName() : "",
                dto.formatId(),
                dto.formatName() != null && !dto.formatName().isBlank() ? dto.formatName() : "",
                dto.languageId(),
                dto.languageName() != null && !dto.languageName().isBlank() ? dto.languageName() : "",
                dto.isbn(),
                dto.pages(),
                dto.publicationYear(),
                dto.editionNumber(),
                null,
                null,
                List.of());
    }
}
