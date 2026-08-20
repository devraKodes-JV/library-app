package com.library.books.infrastructure.persistence.repository.jpa;

import java.util.List;

import com.library.books.infrastructure.persistence.entity.WorkAuthorEntity;
import com.library.kernel.jpa.CrudRepository;

public interface WorkAuthorJpaRepository<T, ID> extends CrudRepository<T, ID> {
    List<WorkAuthorEntity> findByWorkId(Long workId);
    List<WorkAuthorEntity> findByAuthorId(Long authorId);
    void deleteByWorkId(Long workId);
    void deleteByAuthorId(Long authorId);
    java.util.List<WorkAuthorEntity> findByWorkIds(java.util.List<Long> workIds);
    void saveWorkAuthor(Long workId, Long authorId, Long authorRoleId);
}
