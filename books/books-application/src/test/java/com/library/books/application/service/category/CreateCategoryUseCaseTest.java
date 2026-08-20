package com.library.books.application.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.category.CreateCategoryCommand;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.validation.CategoryValidator;
import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Category;

class CreateCategoryUseCaseTest {

    @Test
    void createCategory_returnsSavedCategory() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryRepository, validator);

        CreateCategoryCommand command = new CreateCategoryCommand("FIC", "Fiction", "Fictional works", null);
        CategoryResponseDTO result = useCase.execute(command);

        assertEquals("FIC", result.code());
        assertEquals("Fiction", result.name());
        assertEquals("Fictional works", result.description());
        assertEquals(1L, result.id());
    }

    @Test
    void createCategory_assignsId() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryRepository, validator);

        CreateCategoryCommand command = new CreateCategoryCommand("NONFIC", "Non-Fiction", null, null);
        CategoryResponseDTO result = useCase.execute(command);

        assertTrue(result.id() > 0);
    }

    @Test
    void createCategory_setsParentId() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryRepository, validator);

        categoryRepository.save(Category.withoutId("PARENT", "Parent", null, null));
        CreateCategoryCommand command = new CreateCategoryCommand("CHILD", "Child", null, 1L);
        CategoryResponseDTO result = useCase.execute(command);

        assertEquals(1L, result.parentId());
    }

    @Test
    void createCategory_failsOnValidationError() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryRepository, validator);

        CreateCategoryCommand command = new CreateCategoryCommand("", "", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }

    @Test
    void createCategory_failsOnInvalidCode() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryRepository, validator);

        categoryRepository.save(Category.withoutId("FIC", "Fiction", null, null));
        CreateCategoryCommand command = new CreateCategoryCommand("FIC-001", "Fiction Duplicate", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertTrue(ex.getFieldErrors().containsKey("code"));
        assertEquals("Code must be alphanumeric and 50 characters or less.", ex.getFieldErrors().get("code"));
    }
}
