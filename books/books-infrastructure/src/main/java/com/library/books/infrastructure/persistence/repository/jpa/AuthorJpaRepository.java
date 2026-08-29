package com.library.books.infrastructure.persistence.repository.jpa;

import java.util.List;

import com.library.books.domain.model.Author;
import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByCode;

public interface AuthorJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByCode<T> {
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
    java.util.Map<Long, String> findFullNamesByIds(java.util.List<Long> ids);
    java.util.Optional<Author> findDetailById(Long id);
    List<T> findAll(String status);
    void softDeleteWorkAuthorsByAuthorId(Long authorId);
    void softDeleteEditionAuthorsByAuthorId(Long authorId);
    java.util.List<Long> findWorkIdsByAuthorId(Long authorId);
    void softDeleteWorksByIds(java.util.List<Long> workIds);
    void softDeleteEditionsByWorkIds(java.util.List<Long> workIds);
    void reactivateById(Long id);
    void reactivateWorksByAuthorId(Long authorId);
}
