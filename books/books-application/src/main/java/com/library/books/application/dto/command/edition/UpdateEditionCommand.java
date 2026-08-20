package com.library.books.application.dto.command.edition;

import java.util.List;

public record UpdateEditionCommand(
        Long id,
        Long workId,
        Long publisherId,
        Long formatId,
        Long languageId,
        String isbn,
        Integer pages,
        Integer publicationYear,
        String editionNumber,
        List<String> authorIds,
        List<String> authorRoleIds) {
}
