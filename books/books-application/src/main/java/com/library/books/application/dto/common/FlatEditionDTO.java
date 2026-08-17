package com.library.books.application.dto.common;

public record FlatEditionDTO(
        Long id,
        String workTitle,
        String publisherName,
        String formatName,
        String languageName,
        String isbn,
        Integer pages,
        Integer publicationYear,
        String editionNumber) {
}
