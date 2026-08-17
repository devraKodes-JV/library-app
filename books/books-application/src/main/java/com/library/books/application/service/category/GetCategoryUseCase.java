package com.library.books.application.service.category;

import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.model.Category;
import com.library.books.domain.port.out.CategoryRepository;

import com.library.books.application.dto.response.category.CategoryResponseDTO;

public class GetCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public GetCategoryUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO execute(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(String.valueOf(id)));
        return CategoryResponseDTO.of(category);
    }
}
