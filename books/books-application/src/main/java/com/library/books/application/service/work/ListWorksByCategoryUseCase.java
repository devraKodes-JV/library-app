package com.library.books.application.service.work;

import java.util.List;

import com.library.books.domain.model.Work;
import com.library.books.domain.model.WorkAuthor;
import com.library.books.domain.port.out.CategoryRepository;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.domain.port.out.WorkRepository;

import com.library.books.application.dto.response.work.WorkResponseDTO;

public class ListWorksByCategoryUseCase {

    private final WorkRepository workRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;

    public ListWorksByCategoryUseCase(WorkRepository workRepository, LanguageRepository languageRepository, CategoryRepository categoryRepository) {
        this.workRepository = workRepository;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<WorkResponseDTO> execute(Long categoryId) {
        return workRepository.findByCategoryId(categoryId).stream()
                .map(this::toWorkDTO)
                .toList();
    }

    private WorkResponseDTO toWorkDTO(Work work) {
        String languageName = work.getOriginalLanguageId() != null
                ? languageRepository.findNamesByIds(List.of(work.getOriginalLanguageId()))
                    .getOrDefault(work.getOriginalLanguageId(), "")
                : "";
        String categoryName = work.getCategoryId() != null
                ? categoryRepository.findNamesByIds(List.of(work.getCategoryId()))
                    .getOrDefault(work.getCategoryId(), "")
                : "";
        return WorkResponseDTO.of(
                work.getId(),
                work.getTitle(),
                work.getSubtitle(),
                languageName,
                categoryName,
                List.of()
        );
    }
}
