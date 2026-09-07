package com.library.books.application.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.category.UpdateCategoryCommand;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.validation.CategoryValidator;
import com.library.books.application.service.FakeCodeGenerationService;
import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Category;

class UpdateCategoryUseCaseTest {

    @Test
    void updateCategory_returnsUpdatedCategory() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        UpdateCategoryUseCase useCase = new UpdateCategoryUseCase(categoryRepository, validator);

        Category saved = categoryRepository.save(Category.withoutId("FIC", "Fiction", "Fictional works", null));
        UpdateCategoryCommand command = new UpdateCategoryCommand(saved.getId(), "Updated Fiction", "Updated description", null);

        CategoryResponseDTO result = useCase.execute(command);

        assertEquals("Updated Fiction", result.name());
        assertEquals("Updated description", result.description());
        assertEquals("FIC", result.code());
    }

    @Test
    void updateCategory_throwsWhenNotFound() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        UpdateCategoryUseCase useCase = new UpdateCategoryUseCase(categoryRepository, validator);

        UpdateCategoryCommand command = new UpdateCategoryCommand(999L, "Unknown", null, null);

        assertThrows(CategoryNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void updateCategory_failsOnValidationError() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        UpdateCategoryUseCase useCase = new UpdateCategoryUseCase(categoryRepository, validator);

        Category saved = categoryRepository.save(Category.withoutId("FIC", "Fiction", "Fictional works", null));
        UpdateCategoryCommand command = new UpdateCategoryCommand(saved.getId(), "", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }
}
