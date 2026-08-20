package com.library.books.application.service.category;

import java.util.Map;

import com.library.books.application.dto.command.category.CreateCategoryCommand;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.validation.CategoryValidator;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Category;
import com.library.books.domain.port.out.CategoryRepository;

public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;

    public CreateCategoryUseCase(CategoryRepository categoryRepository, CategoryValidator categoryValidator) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
    }

    public CategoryResponseDTO execute(CreateCategoryCommand command) {
        if (categoryRepository.findByCode(command.code()).isPresent()) {
            throw new ValidationException(Map.of("code", "Code already exists"));
        }
        Category category = Category.withoutId(command.code(), command.name(), command.description(), command.parentId());
        categoryValidator.validate(category);
        Category saved = categoryRepository.save(category);
        return CategoryResponseDTO.of(saved);
    }
}
