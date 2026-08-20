package com.library.books.application.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.category.UpdateCategoryCommand;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.validation.CategoryValidator;
import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Category;

class UpdateCategoryUseCaseTest {

    @Test
    void updateCategory_returnsUpdatedCategory() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        UpdateCategoryUseCase useCase = new UpdateCategoryUseCase(categoryRepository, validator);

        Category saved = categoryRepository.save(Category.withoutId("FIC", "Fiction", "Old desc", null));
        UpdateCategoryCommand command = new UpdateCategoryCommand(saved.getId(), "FIC", "Fiction Updated", "New desc", null);
        CategoryResponseDTO result = useCase.execute(command);

        assertEquals("FIC", result.code());
        assertEquals("Fiction Updated", result.name());
        assertEquals("New desc", result.description());
    }

    @Test
    void updateCategory_changesParent() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        UpdateCategoryUseCase useCase = new UpdateCategoryUseCase(categoryRepository, validator);

        Category parent = categoryRepository.save(Category.withoutId("PARENT", "Parent", null, null));
        Category saved = categoryRepository.save(Category.withoutId("CHILD", "Child", null, null));
        UpdateCategoryCommand command = new UpdateCategoryCommand(saved.getId(), "CHILD", "Child", null, parent.getId());
        CategoryResponseDTO result = useCase.execute(command);

        assertEquals(parent.getId(), result.parentId());
    }

    @Test
    void updateCategory_throwsWhenNotFound() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        UpdateCategoryUseCase useCase = new UpdateCategoryUseCase(categoryRepository, validator);

        UpdateCategoryCommand command = new UpdateCategoryCommand(999L, "FIC", "Fiction", null, null);

        assertThrows(CategoryNotFoundException.class, () -> useCase.execute(command));
    }

    @Test
    void updateCategory_failsOnValidationError() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        UpdateCategoryUseCase useCase = new UpdateCategoryUseCase(categoryRepository, validator);

        Category saved = categoryRepository.save(Category.withoutId("FIC", "Fiction", null, null));
        UpdateCategoryCommand command = new UpdateCategoryCommand(saved.getId(), "", "", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }

    @Test
    void updateCategory_failsOnInvalidCode() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        UpdateCategoryUseCase useCase = new UpdateCategoryUseCase(categoryRepository, validator);

        categoryRepository.save(Category.withoutId("EXISTING", "Existing", null, null));
        Category saved = categoryRepository.save(Category.withoutId("FIC", "Fiction", null, null));
        UpdateCategoryCommand command = new UpdateCategoryCommand(saved.getId(), "INVALID-CODE", "Fiction", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
        assertEquals("Code must be alphanumeric and 50 characters or less.", ex.getFieldErrors().get("code"));
    }
}
