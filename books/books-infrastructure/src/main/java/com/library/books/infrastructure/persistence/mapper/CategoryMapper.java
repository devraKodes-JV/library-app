package com.library.books.infrastructure.persistence.mapper;

import com.library.books.domain.model.Category;
import com.library.books.infrastructure.persistence.entity.CategoryEntity;

public final class CategoryMapper {

    private CategoryMapper() {
    }

    public static Category toDomain(CategoryEntity e) {
        if (e == null) {
            return null;
        }
        return new Category(
                e.getId(),
                e.getCode(),
                e.getName(),
                e.getDescription(),
                e.getParentId(),
                e.getCreatedAt(),
                e.getUpdatedAt());
    }

    public static CategoryEntity toEntity(Category c) {
        if (c == null) {
            return null;
        }
        return new CategoryEntity(
                c.getId(),
                c.getCode(),
                c.getName(),
                c.getDescription(),
                c.getParentId(),
                true);
    }
}
