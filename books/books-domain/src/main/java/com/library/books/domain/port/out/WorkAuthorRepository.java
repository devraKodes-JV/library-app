package com.library.books.domain.port.out;

import java.util.List;

import com.library.books.domain.model.WorkAuthor;

public interface WorkAuthorRepository {
    WorkAuthor save(WorkAuthor workAuthor);
    void deleteById(Long id);
    List<WorkAuthor> findByWorkId(Long workId);
    List<WorkAuthor> findByAuthorId(Long authorId);
    void deleteByWorkId(Long workId);
    void deleteByAuthorId(Long authorId);
    java.util.List<com.library.books.domain.model.WorkAuthor> findByWorkIds(java.util.List<Long> workIds);
}
