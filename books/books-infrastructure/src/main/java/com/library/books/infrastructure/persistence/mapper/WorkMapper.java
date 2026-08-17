package com.library.books.infrastructure.persistence.mapper;

import java.util.List;

import com.library.books.domain.model.Work;
import com.library.books.domain.model.WorkAuthor;
import com.library.books.infrastructure.persistence.entity.WorkEntity;

public final class WorkMapper {

    private WorkMapper() {
    }

    public static Work toDomain(WorkEntity e) {
        if (e == null) {
            return null;
        }
        return new Work(
                e.getId(),
                e.getTitle(),
                e.getSubtitle(),
                e.getOriginalLanguageId(),
                e.getCategoryId(),
                e.getSummary(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                null);
    }

    public static WorkEntity toEntity(Work w) {
        if (w == null) {
            return null;
        }
        return new WorkEntity(
                w.getId(),
                w.getTitle(),
                w.getSubtitle(),
                w.getOriginalLanguageId(),
                w.getCategoryId(),
                w.getSummary(),
                true);
    }
}
