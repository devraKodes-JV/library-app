package com.library.books.application.service.work;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.Category;
import com.library.books.domain.port.out.CategoryRepository;

class FakeCategoryRepository implements CategoryRepository {

    private final Map<Long, Category> store = new LinkedHashMap<>();
    private long nextId = 1L;

    @Override
    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Category> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Category> findByCode(String code) {
        return Optional.empty();
    }

    @Override
    public Category save(Category category) {
        if (category.getId() == null) {
            Category withId = new Category(nextId++, category.getCode(), category.getName(),
                    category.getDescription(), category.getParentId(),
                    category.getCreatedAt(), category.getUpdatedAt());
            store.put(withId.getId(), withId);
            return withId;
        }
        store.put(category.getId(), category);
        return category;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        Map<Long, String> result = new LinkedHashMap<>();
        for (Long id : ids) {
            Category cat = store.get(id);
            if (cat != null) {
                result.put(id, cat.getName());
            }
        }
        return result;
    }

    @Override
    public void nullifyParent(Long parentId) {
    }
}
