package com.library.books.application.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.library.books.domain.exception.CategoryNotFoundException;
import com.library.books.domain.model.Category;
import com.library.books.domain.model.Work;

class DeleteCategoryUseCaseTest {

    @Test
    void deleteCategory_removesCategory() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteCategoryUseCase useCase = new DeleteCategoryUseCase(categoryRepository, workRepository);

        Category saved = categoryRepository.save(Category.withoutId("FIC", "Fiction", null, null));
        useCase.execute(new com.library.books.application.dto.command.category.DeleteCategoryCommand(saved.getId()));

        assertTrue(categoryRepository.findById(saved.getId()).isEmpty());
    }

    @Test
    void deleteCategory_nullifiesWorksCategoryId() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteCategoryUseCase useCase = new DeleteCategoryUseCase(categoryRepository, workRepository);

        Category saved = categoryRepository.save(Category.withoutId("FIC", "Fiction", null, null));
        Work work = Work.withoutId("Test Work", null, null, saved.getId(), null);
        workRepository.save(work);

        useCase.execute(new com.library.books.application.dto.command.category.DeleteCategoryCommand(saved.getId()));

        List<Work> works = workRepository.findByCategoryId(saved.getId());
        assertTrue(works.isEmpty());

        Work updated = workRepository.findById(work.getId()).orElseThrow();
        assertEquals(null, updated.getCategoryId());
    }

    @Test
    void deleteCategory_nullifiesChildCategoryParentId() {
        FakeCategoryRepository categoryRepository = new FakeCategoryRepository();
        FakeWorkRepository workRepository = new FakeWorkRepository();
        DeleteCategoryUseCase useCase = new DeleteCategoryUseCase(categoryRepository, workRepository);

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
        DeleteCategoryUseCase useCase = new DeleteCategoryUseCase(categoryRepository, workRepository);

        assertThrows(CategoryNotFoundException.class,
                () -> useCase.execute(new com.library.books.application.dto.command.category.DeleteCategoryCommand(999L)));
    }
}
