package com.library.books.application.dto.response.work;

import java.util.List;

import com.library.books.domain.dto.common.FlatAuthorDTO;
import com.library.books.domain.dto.common.FlatEditionDTO;
import com.library.books.domain.dto.query.WorkWithRelationsDTO;

public record WorkDetailResponseDTO(
        Long id,
        String title,
        String subtitle,
        String summary,
        String languageName,
        String categoryName,
        List<FlatEditionDTO> editions,
        List<FlatAuthorDTO> authors) {

    public static WorkDetailResponseDTO from(Long id, String title, String subtitle, String summary, String languageName, String categoryName,
                                     List<FlatEditionDTO> editions,
                                     List<FlatAuthorDTO> authors) {
        return new WorkDetailResponseDTO(
                id,
                title,
                subtitle,
                summary != null && !summary.isBlank() ? summary : "",
                languageName != null && !languageName.isBlank() ? languageName : "",
                categoryName != null && !categoryName.isBlank() ? categoryName : "",
                editions != null ? editions : List.of(),
                authors != null ? authors : List.of());
    }

    public static WorkDetailResponseDTO from(WorkWithRelationsDTO data) {
        return new WorkDetailResponseDTO(
                data.id(),
                data.title(),
                data.subtitle(),
                data.summary() != null && !data.summary().isBlank() ? data.summary() : "",
                data.languageName() != null && !data.languageName().isBlank() ? data.languageName() : "",
                data.categoryName() != null && !data.categoryName().isBlank() ? data.categoryName() : "",
                List.of(),
                data.authors() != null ? data.authors() : List.of());
    }
}
