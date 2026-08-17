package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.Category;

public interface CategoryRepository {
    Optional<Category> findById(Long id);
    List<Category> findAll();
    Optional<Category> findByCode(String code);
    Category save(Category category);
    void deleteById(Long id);
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
}
