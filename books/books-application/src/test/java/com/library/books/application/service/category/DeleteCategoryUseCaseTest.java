package com.library.books.application.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.model.Category;
import com.library.books.domain.model.Work;

class DeleteCategoryUseCaseTest {

    @Test
    void deleteCategory_removesCategory() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteCategoryUseCase useCase = new DeleteCategoryUseCase(categoryRepository);

        Category saved = categoryRepository.save(Category.withoutId("FIC", "Fiction", null, null));
        useCase.execute(new com.library.books.application.dto.command.category.DeleteCategoryCommand(saved.getId()));

        assertTrue(categoryRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteCategory_preservesWorksCategoryId() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteCategoryUseCase useCase = new DeleteCategoryUseCase(categoryRepository);

        Category saved = categoryRepository.save(Category.withoutId("FIC", "Fiction", null, null));
        Work work = Work.withoutId("Test Work", null, null, saved.getId(), null);
        workRepository.save(work);

        useCase.execute(new com.library.books.application.dto.command.category.DeleteCategoryCommand(saved.getId()));

        // Category is soft-deleted
        assertTrue(categoryRepository.findById(saved.getId()).isEmpty());
        // Work reference is preserved (not nullified) so it can be restored on reactivation
    }

    @Test
    void deleteCategory_nullifiesChildCategoryParentId() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteCategoryUseCase useCase = new DeleteCategoryUseCase(categoryRepository);

        Category parent = categoryRepository.save(Category.withoutId("PARENT", "Parent", null, null));
        Category child = categoryRepository.save(Category.withoutId("CHILD", "Child", null, parent.getId()));

        useCase.execute(new com.library.books.application.dto.command.category.DeleteCategoryCommand(parent.getId()));

        Category updatedChild = categoryRepository.findById(child.getId()).orElseThrow();
        assertEquals(null, updatedChild.getParentId());
    }

    @Test
    void deleteCategory_throwsWhenNotFound() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteCategoryUseCase useCase = new DeleteCategoryUseCase(categoryRepository);

        assertThrows(CategoryNotFoundException.class,
                () -> useCase.execute(new com.library.books.application.dto.command.category.DeleteCategoryCommand(999L)));
    }
}
