package com.library.books.application.dto.command.category;

public record UpdateCategoryCommand(
        Long id,
        String code,
        String name,
        String description,
        Long parentId) {
}
