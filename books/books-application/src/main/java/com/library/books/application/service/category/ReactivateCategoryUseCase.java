package com.library.books.application.service.category;

import com.library.books.application.dto.command.category.ReactivateCategoryCommand;
import com.library.books.domain.port.out.CategoryRepository;
import com.library.books.domain.port.out.WorkRepository;

public class ReactivateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final WorkRepository workRepository;

    public ReactivateCategoryUseCase(CategoryRepository categoryRepository, WorkRepository workRepository) {
        this.categoryRepository = categoryRepository;
        this.workRepository = workRepository;
    }

    public void execute(ReactivateCategoryCommand command) {
        categoryRepository.reactivateById(command.id());
        categoryRepository.reactivateWorksByCategoryId(command.id());
    }
}
