package com.library.books.application.service.work;

import java.util.List;

import com.library.books.application.dto.response.work.WorkResponseDTO;
import com.library.books.domain.dto.query.WorkWithRelationsDTO;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.model.WorkAuthor;
import com.library.books.domain.port.out.WorkRepository;

public class GetWorkUseCase {

    private final WorkRepository workRepository;

    public GetWorkUseCase(WorkRepository workRepository) {
        this.workRepository = workRepository;
    }

    public WorkResponseDTO execute(Long id) {
        WorkWithRelationsDTO data = workRepository.findByIdWithRelations(id);
        if (data == null) {
            throw new WorkNotFoundException(id);
        }
        String languageName = data.languageName() != null && !data.languageName().isBlank() ? data.languageName() : "";
        String categoryName = data.categoryName() != null && !data.categoryName().isBlank() ? data.categoryName() : "";
        List<WorkAuthor> authors = data.authors() != null ? data.authors().stream()
                .map(a -> new WorkAuthor(null, null, a.id(), a.role(), null, null))
                .toList()
                : List.of();
        return new WorkResponseDTO(
                data.id(),
                data.title(),
                data.subtitle(),
                null,
                languageName,
                null,
                categoryName,
                data.summary() != null && !data.summary().isBlank() ? data.summary() : "",
                null,
                null,
                authors
        );
    }
}
