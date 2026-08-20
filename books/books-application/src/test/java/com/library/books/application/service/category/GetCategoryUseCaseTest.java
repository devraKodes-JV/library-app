package com.library.books.application.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.model.Category;

class GetCategoryUseCaseTest {

    @Test
    void getCategory_returnsCategory() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        GetCategoryUseCase useCase = new GetCategoryUseCase(categoryRepository);

        Category saved = categoryRepository.save(Category.withoutId("FIC", "Fiction", "Fictional works", null));
        CategoryResponseDTO result = useCase.execute(saved.getId());

        assertEquals("FIC", result.code());
        assertEquals("Fiction", result.name());
    }

    @Test
    void getCategory_throwsWhenNotFound() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        GetCategoryUseCase useCase = new GetCategoryUseCase(categoryRepository);

        assertThrows(CategoryNotFoundException.class, () -> useCase.execute(999L));
    }
}
