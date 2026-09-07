package com.library.books.application.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.application.dto.command.category.CreateCategoryCommand;
import com.library.books.application.dto.response.category.CategoryResponseDTO;
import com.library.books.application.validation.CategoryValidator;
import com.library.books.application.service.FakeCodeGenerationService;
import com.library.books.domain.exception.ValidationException;
import com.library.books.domain.model.Category;

class CreateCategoryUseCaseTest {

    @Test
    void createCategory_returnsSavedCategory() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        FakeCodeGenerationService codeGen = new FakeCodeGenerationService("CAT");
        CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryRepository, validator, codeGen);

        CreateCategoryCommand command = new CreateCategoryCommand("Fiction", "Fictional works", null);
        CategoryResponseDTO result = useCase.execute(command);

        assertEquals("CAT-1", result.code());
        assertEquals("Fiction", result.name());
        assertEquals("Fictional works", result.description());
        assertEquals(1L, result.id());
    }

    @Test
    void createCategory_assignsId() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        FakeCodeGenerationService codeGen = new FakeCodeGenerationService("CAT");
        CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryRepository, validator, codeGen);

        CreateCategoryCommand command = new CreateCategoryCommand("Non-Fiction", null, null);
        CategoryResponseDTO result = useCase.execute(command);

        assertTrue(result.id() > 0);
    }

    @Test
    void createCategory_setsParentId() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        FakeCodeGenerationService codeGen = new FakeCodeGenerationService("CAT");
        CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryRepository, validator, codeGen);

        categoryRepository.save(Category.withoutId("PARENT", "Parent", null, null));
        CreateCategoryCommand command = new CreateCategoryCommand("Child", null, 1L);
        CategoryResponseDTO result = useCase.execute(command);

        assertEquals(1L, result.parentId());
    }

    @Test
    void createCategory_failsOnValidationError() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        CategoryValidator validator = new CategoryValidator();
        FakeCodeGenerationService codeGen = new FakeCodeGenerationService("CAT");
        CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryRepository, validator, codeGen);

        CreateCategoryCommand command = new CreateCategoryCommand("", null, null);

        ValidationException ex = assertThrows(ValidationException.class, () -> useCase.execute(command));
        assertFalse(ex.getFieldErrors().containsKey("code"));
        assertTrue(ex.getFieldErrors().containsKey("name"));
    }
}
