package com.library.books.application.service.category;

import com.library.books.application.dto.command.category.UpdateCategoryCommand;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.validation.CategoryValidator;
import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.model.Category;
import com.library.books.domain.port.out.CategoryRepository;

public class UpdateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;

    public UpdateCategoryUseCase(CategoryRepository categoryRepository, CategoryValidator categoryValidator) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
    }

    public CategoryResponseDTO execute(UpdateCategoryCommand command) {
        Category existing = categoryRepository.findById(command.id())
                .orElseThrow(() -> new CategoryNotFoundException(String.valueOf(command.id())));
        existing.setCode(command.code());
        existing.setName(command.name());
        existing.setDescription(command.description());
        existing.setParentId(command.parentId());
        categoryValidator.validate(existing);
        Category saved = categoryRepository.save(existing);
        return CategoryResponseDTO.of(saved);
    }
}
