package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.dto.query.WorkWithRelationsDTO;
import com.library.books.domain.model.Work;

public interface WorkRepository {
    Optional<Work> findById(Long id);
    List<Work> findAll();
    List<Work> findByCategoryId(Long categoryId);
    List<Work> findByOriginalLanguageId(Long languageId);
    Work save(Work work);
    void deleteById(Long id);
    List<Work> findByIds(List<Long> ids);
    List<Work> findByAuthorId(Long authorId);
    boolean existsLanguage(Long id);
    boolean existsCategory(Long id);
    void saveWorkAuthor(Long workId, Long authorId);
    void deleteWorkAuthorsByWorkId(Long workId);
    WorkWithRelationsDTO findByIdWithRelations(Long id);
}
