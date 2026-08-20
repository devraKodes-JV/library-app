package com.library.books.domain.dto.common;

public record FlatAuthorDTO(
        Long id,
        String displayName,
        Long authorRoleId,
        String authorRoleName) {
}
