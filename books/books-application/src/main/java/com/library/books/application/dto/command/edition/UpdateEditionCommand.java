package com.library.books.application.dto.command.edition;

public record UpdateEditionCommand(
        Long id,
        Long workId,
        Long publisherId,
        Long formatId,
        Long languageId,
        String isbn,
        Integer pages,
        Integer publicationYear,
        String editionNumber) {
}
