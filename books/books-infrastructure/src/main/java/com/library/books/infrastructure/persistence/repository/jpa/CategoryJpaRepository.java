package com.library.books.infrastructure.persistence.repository.jpa;

import com.library.books.domain.model.Category;
import com.library.books.domain.model.Work;
import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByCode;

public interface CategoryJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByCode<T> {
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
    java.util.Optional<Category> findDetailById(Long id);
    java.util.List<Work> findRelatedWorks(Long categoryId);
}
