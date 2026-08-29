package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.dto.query.WorkWithRelationsDTO;
import com.library.books.domain.model.Work;

public interface WorkRepository {
    Optional<Work> findById(Long id);
    List<Work> findAll();
    List<Work> findAll(String status);
    List<Work> findByCategoryId(Long categoryId);
    List<Work> findByOriginalLanguageId(Long languageId);
    Work save(Work work);
    void deleteById(Long id);
    List<Work> findByIds(List<Long> ids);
    List<Work> findByAuthorId(Long authorId);
    boolean existsLanguage(Long id);
    boolean existsCategory(Long id);
    void saveWorkAuthor(Long workId, Long authorId, Long authorRoleId);
    void deleteWorkAuthorsByWorkId(Long workId);
    WorkWithRelationsDTO findByIdWithRelations(Long id);
    void nullifyOriginalLanguage(Long languageId);
    void nullifyCategory(Long categoryId);
    void softDeleteEditionsByWorkIds(List<Long> workIds);
    void softDeleteWorksByIds(List<Long> ids);
    long countActiveAuthorsByWorkId(Long workId);
    List<Long> findAuthorIdsByWorkId(Long workId);
    List<Long> findWorkIdsByCategoryId(Long categoryId);
    void reactivateById(Long id);
    void reactivateEditionsByWorkId(Long workId);
}
