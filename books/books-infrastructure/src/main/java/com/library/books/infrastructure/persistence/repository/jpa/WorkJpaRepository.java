package com.library.books.infrastructure.persistence.repository.jpa;

import java.util.List;

import com.library.books.infrastructure.persistence.entity.WorkEntity;
import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByCode;

public interface WorkJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByCode<T> {
    List<WorkEntity> findByCategoryId(Long categoryId);
    List<WorkEntity> findByOriginalLanguageId(Long languageId);
    java.util.List<WorkEntity> findByIds(java.util.List<Long> ids);
    List<WorkEntity> findByAuthorId(Long authorId);
    boolean existsLanguage(Long id);
    boolean existsCategory(Long id);
    void saveWorkAuthor(Long workId, Long authorId, Long authorRoleId);
    void deleteWorkAuthorsByWorkId(Long workId);
}
