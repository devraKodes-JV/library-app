package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.Edition;

public interface EditionRepository {
    Optional<Edition> findById(Long id);
    List<Edition> findAll();
    List<Edition> findByWorkId(Long workId);
    List<Edition> findByPublisherId(Long publisherId);
    List<Edition> findByFormatId(Long formatId);
    Edition save(Edition edition);
    void deleteById(Long id);
}
