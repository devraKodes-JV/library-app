package com.library.books.application.service.work;

import java.util.List;

import com.library.books.domain.model.Work;
import com.library.books.domain.model.WorkAuthor;
import com.library.books.domain.port.out.CategoryRepository;
import com.library.books.domain.port.out.LanguageRepository;
import com.library.books.domain.port.out.WorkRepository;

import com.library.kernel.web.Page;
import com.library.books.application.dto.response.work.WorkResponseDTO;

public class ListWorksUseCase {

    private final WorkRepository workRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;

    public ListWorksUseCase(WorkRepository workRepository, LanguageRepository languageRepository, CategoryRepository categoryRepository) {
        this.workRepository = workRepository;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<WorkResponseDTO> execute(int page, int size) {
        List<Work> allWorks = workRepository.findAll();
        long totalElements = allWorks.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int start = page * size;
        int end = Math.min(start + size, allWorks.size());
        List<Work> pagedWorks = start >= end ? List.of() : allWorks.subList(start, end);

        List<WorkResponseDTO> items = pagedWorks.stream()
                .map(this::toWorkDTO)
                .toList();
        return Page.of(items, totalElements, page, size);
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
