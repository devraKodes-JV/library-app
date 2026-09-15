package com.library.books.domain.dto.response.edition;

import java.math.BigDecimal;

public record EditionWithNamesDTO(
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
        BigDecimal dailyPrice) {
}
