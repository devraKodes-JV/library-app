package com.library.books.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.Category;
import com.library.books.domain.port.out.CategoryRepository;
import com.library.books.infrastructure.persistence.entity.CategoryEntity;
import com.library.books.infrastructure.persistence.mapper.CategoryMapper;
import com.library.books.infrastructure.persistence.repository.jpa.CategoryJpaRepository;

public class CategoryPersistenceAdapter implements CategoryRepository {

    private final CategoryJpaRepository<CategoryEntity, Long> categoryJpaRepository;

    public CategoryPersistenceAdapter(CategoryJpaRepository<CategoryEntity, Long> categoryJpaRepository) {
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id)
                .map(CategoryMapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return categoryJpaRepository.findAll().stream()
                .map(CategoryMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Category> findByCode(String code) {
        return categoryJpaRepository.findByCode(code)
                .map(CategoryMapper::toDomain);
    }

    @Override
    public Category save(Category category) {
        CategoryEntity entity = CategoryMapper.toEntity(category);
        CategoryEntity saved = categoryJpaRepository.save(entity);
        return CategoryMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        categoryJpaRepository.deleteById(id);
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        return categoryJpaRepository.findNamesByIds(ids);
    }

    @Override
    public void nullifyParent(Long parentId) {
        ((com.library.books.infrastructure.persistence.repository.hibernate.HibernateCategoryRepository) categoryJpaRepository).nullifyParent(parentId);
    }
}
