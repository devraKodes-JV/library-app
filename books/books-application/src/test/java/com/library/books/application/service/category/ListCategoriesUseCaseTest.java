package com.library.books.application.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.domain.model.Category;

class ListCategoriesUseCaseTest {

    @Test
    void listCategories_returnsAll() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        ListCategoriesUseCase useCase = new ListCategoriesUseCase(categoryRepository);

        categoryRepository.save(Category.withoutId("FIC", "Fiction", null, null));
        categoryRepository.save(Category.withoutId("NON-FIC", "Non-Fiction", null, null));

        List<CategoryResponseDTO> result = useCase.execute();

        assertEquals(2, result.size());
        assertEquals("FIC", result.get(0).code());
        assertEquals("NON-FIC", result.get(1).code());
    }

    @Test
    void listCategories_returnsEmptyWhenNone() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        ListCategoriesUseCase useCase = new ListCategoriesUseCase(categoryRepository);

        List<CategoryResponseDTO> result = useCase.execute();

        assertTrue(result.isEmpty());
    }
}
