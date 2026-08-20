package com.library.books.application.dto.command.work;

import java.util.List;

public record UpdateWorkCommand(
        Long id,
        String title,
        String subtitle,
        Long originalLanguageId,
        Long categoryId,
        String summary,
        List<String> authorIds,
        List<String> authorRoleIds) {
}
