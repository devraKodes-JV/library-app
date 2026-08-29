package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.dto.query.AuthorWithWorksDTO;
import com.library.books.domain.model.Author;

public interface AuthorRepository {
    Optional<Author> findById(Long id);
    List<Author> findAll();
    List<Author> findAll(String status);
    Author save(Author author);
    void deleteById(Long id);
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
    java.util.Map<Long, String> findFullNamesByIds(java.util.List<Long> ids);
    AuthorWithWorksDTO findByIdWithWorks(Long id);
    void softDeleteWorkAuthorsByAuthorId(Long authorId);
    void softDeleteEditionAuthorsByAuthorId(Long authorId);
    java.util.List<Long> findWorkIdsByAuthorId(Long authorId);
    void softDeleteWorksByIds(java.util.List<Long> workIds);
    void softDeleteEditionsByWorkIds(java.util.List<Long> workIds);
    void reactivateById(Long id);
    void reactivateWorksByAuthorId(Long authorId);
}
