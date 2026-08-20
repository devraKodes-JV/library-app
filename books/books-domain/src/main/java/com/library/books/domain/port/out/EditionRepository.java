package com.library.books.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.Edition;
import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.dto.response.edition.EditionWithNamesDTO;

public interface EditionRepository {
    Optional<Edition> findById(Long id);
    List<Edition> findAll();
    List<Edition> findByWorkId(Long workId);
    List<Edition> findByPublisherId(Long publisherId);
    List<Edition> findByFormatId(Long formatId);
    Edition save(Edition edition);
    void deleteById(Long id);
    long countActiveByWorkId(Long workId);
    long countActiveByPublisherId(Long publisherId);
    long countActiveByFormatId(Long formatId);
    long countActiveByLanguageId(Long languageId);
    List<EditionWithNamesDTO> findByWorkIdWithDetails(Long workId);
    List<EditionAuthor> findEditionAuthorsByEditionId(Long editionId);
}
