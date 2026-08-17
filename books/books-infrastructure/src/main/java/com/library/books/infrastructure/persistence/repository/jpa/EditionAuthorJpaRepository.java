package com.library.books.infrastructure.persistence.repository.jpa;

import java.util.List;

import com.library.books.infrastructure.persistence.entity.EditionAuthorEntity;
import com.library.kernel.jpa.CrudRepository;

public interface EditionAuthorJpaRepository<T, ID> extends CrudRepository<T, ID> {
    List<EditionAuthorEntity> findByEditionId(Long editionId);
    List<EditionAuthorEntity> findByAuthorId(Long authorId);
    void deleteByEditionId(Long editionId);
    void deleteByAuthorId(Long authorId);
}
