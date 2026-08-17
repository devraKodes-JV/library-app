package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.dto.query.AuthorWithWorksDTO;
import com.library.books.domain.model.Author;

public interface AuthorRepository {
    Optional<Author> findById(Long id);
    List<Author> findAll();
    Author save(Author author);
    void deleteById(Long id);
    java.util.Map<Long, String> findNamesByIds(java.util.List<Long> ids);
    java.util.Map<Long, String> findFullNamesByIds(java.util.List<Long> ids);
    AuthorWithWorksDTO findByIdWithWorks(Long id);
}
