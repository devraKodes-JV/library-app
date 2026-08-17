package com.library.books.infrastructure.persistence.repository.jpa;

import com.library.books.domain.model.Author;
import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByCode;

public interface AuthorJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByCode<T> {
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
    java.util.Map<Long, String> findFullNamesByIds(java.util.List<Long> ids);
    java.util.Optional<Author> findDetailById(Long id);
}
