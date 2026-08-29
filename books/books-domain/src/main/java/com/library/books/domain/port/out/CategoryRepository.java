package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.Category;

public interface CategoryRepository {
    Optional<Category> findById(Long id);
    List<Category> findAll();
    List<Category> findAll(String status);
    Optional<Category> findByCode(String code);
    Category save(Category category);
    void deleteById(Long id);
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
    void nullifyParent(Long parentId);
    List<Long> findWorkIdsByCategoryId(Long categoryId);
    void reactivateById(Long id);
    void reactivateWorksByCategoryId(Long categoryId);
}
