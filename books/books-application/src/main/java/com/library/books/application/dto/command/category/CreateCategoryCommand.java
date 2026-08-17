package com.library.books.application.dto.command.category;

public record CreateCategoryCommand(
        String code,
        String name,
        String description,
        Long parentId) {
}
