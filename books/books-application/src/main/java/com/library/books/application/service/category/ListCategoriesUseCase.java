package com.library.books.application.service.category;

import com.library.books.domain.model.Category;
import com.library.books.domain.port.out.CategoryRepository;

import com.library.books.application.dto.response.category.CategoryResponseDTO;

import java.util.List;

public class ListCategoriesUseCase {

    private final CategoryRepository categoryRepository;

    public ListCategoriesUseCase(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponseDTO> execute() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponseDTO::of)
                .toList();
    }
}
