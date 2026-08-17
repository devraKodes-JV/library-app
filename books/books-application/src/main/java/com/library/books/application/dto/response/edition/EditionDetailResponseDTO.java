package com.library.books.application.dto.response.edition;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.library.books.domain.model.Edition;

public record EditionDetailResponseDTO(
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
        List<String> editionAuthors,
        Map<Long, String> authorNames) {

    public static EditionDetailResponseDTO from(Edition edition, String workTitle, String publisherName,
                                        String formatName, String languageName, List<String> editionAuthors, Map<Long, String> authorNames) {
        return new EditionDetailResponseDTO(
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
                editionAuthors != null ? editionAuthors : List.of(),
                authorNames != null ? authorNames : Map.of());
    }

    public static EditionDetailResponseDTO of(Long id, Long workId, String workTitle, Long publisherId, String publisherName,
                                      Long formatId, String formatName, Long languageId, String languageName,
                                      String isbn, Integer pages, Integer publicationYear, String editionNumber,
                                      java.time.Instant createdAt, java.time.Instant updatedAt,
                                      List<String> editionAuthors, Map<Long, String> authorNames) {
        return new EditionDetailResponseDTO(
                id, workId, workTitle, publisherId, publisherName, formatId, formatName, languageId, languageName,
                isbn, pages, publicationYear, editionNumber, createdAt, updatedAt,
                editionAuthors != null ? editionAuthors : List.of(),
                authorNames != null ? authorNames : Map.of());
    }
}
