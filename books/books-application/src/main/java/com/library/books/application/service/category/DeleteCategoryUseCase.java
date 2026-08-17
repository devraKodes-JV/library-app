package com.library.books.application.service.category;

import com.library.books.application.dto.command.category.DeleteCategoryCommand;
import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.model.Category;
import com.library.books.domain.port.out.CategoryRepository;

public class DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public DeleteCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void execute(DeleteCategoryCommand command) {
        Category existing = categoryRepository.findById(command.id())
                .orElseThrow(() -> new CategoryNotFoundException(String.valueOf(command.id())));
        categoryRepository.deleteById(command.id());
    }
}
