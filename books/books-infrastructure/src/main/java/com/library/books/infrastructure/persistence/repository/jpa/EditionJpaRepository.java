package com.library.books.infrastructure.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.Edition;
import com.library.books.infrastructure.persistence.entity.EditionEntity;
import com.library.kernel.jpa.CrudRepository;

public interface EditionJpaRepository<T, ID> extends CrudRepository<T, ID> {
    List<EditionEntity> findByWorkId(Long workId);
    List<EditionEntity> findByPublisherId(Long publisherId);
    List<EditionEntity> findByFormatId(Long formatId);
    List<Edition> findSummariesByWorkId(Long workId);
    List<Edition> findSummariesByPublisherId(Long publisherId);
    List<Edition> findSummariesByFormatId(Long formatId);
    Optional<Edition> findDetailById(Long id);
}
