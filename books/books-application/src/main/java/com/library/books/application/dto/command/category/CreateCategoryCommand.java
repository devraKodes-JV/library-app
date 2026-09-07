package com.library.books.application.dto.command.category;

public record CreateCategoryCommand(
        String name,
        String description,
        Long parentId) {
}
