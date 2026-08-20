package com.library.books.domain.port.out;

import java.util.List;

import com.library.books.domain.model.EditionAuthor;

public interface EditionAuthorRepository {
    EditionAuthor save(EditionAuthor editionAuthor);
    void deleteById(Long id);
    List<EditionAuthor> findByEditionId(Long editionId);
    List<EditionAuthor> findByAuthorId(Long authorId);
    void deleteByEditionId(Long editionId);
    void deleteByAuthorId(Long authorId);
    void saveEditionAuthor(Long editionId, Long authorId, Long authorRoleId);
}
