package com.library.books.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.Edition;
import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.domain.dto.response.edition.EditionWithNamesDTO;
import com.library.books.infrastructure.persistence.entity.EditionEntity;
import com.library.books.infrastructure.persistence.entity.EditionAuthorEntity;
import com.library.books.infrastructure.persistence.mapper.EditionMapper;
import com.library.books.infrastructure.persistence.mapper.EditionAuthorMapper;
import com.library.books.infrastructure.persistence.repository.jpa.EditionJpaRepository;
import com.library.books.infrastructure.persistence.repository.jpa.EditionAuthorJpaRepository;

public class EditionPersistenceAdapter implements EditionRepository {

    private final EditionJpaRepository<EditionEntity, Long> editionJpaRepository;
    private final EditionAuthorJpaRepository<EditionAuthorEntity, Long> editionAuthorJpaRepository;

    public EditionPersistenceAdapter(EditionJpaRepository<EditionEntity, Long> editionJpaRepository, EditionAuthorJpaRepository<EditionAuthorEntity, Long> editionAuthorJpaRepository) {
        this.editionJpaRepository = editionJpaRepository;
        this.editionAuthorJpaRepository = editionAuthorJpaRepository;
    }

    @Override
    public Optional<Edition> findById(Long id) {
        return editionJpaRepository.findById(id)
                .map(EditionMapper::toDomain);
    }

    @Override
    public List<Edition> findAll() {
        return editionJpaRepository.findAll().stream()
                .map(EditionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Edition> findByWorkId(Long workId) {
        return editionJpaRepository.findByWorkId(workId).stream()
                .map(EditionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Edition> findByPublisherId(Long publisherId) {
        return editionJpaRepository.findByPublisherId(publisherId).stream()
                .map(EditionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Edition> findByFormatId(Long formatId) {
        return editionJpaRepository.findByFormatId(formatId).stream()
                .map(EditionMapper::toDomain)
                .toList();
    }

    @Override
    public Edition save(Edition edition) {
        EditionEntity entity = EditionMapper.toEntity(edition);
        EditionEntity saved = editionJpaRepository.save(entity);
        return EditionMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        editionJpaRepository.deleteById(id);
    }

    @Override
    public long countActiveByWorkId(Long workId) {
        return editionJpaRepository.countActiveByWorkId(workId);
    }

    @Override
    public long countActiveByPublisherId(Long publisherId) {
        return editionJpaRepository.countActiveByPublisherId(publisherId);
    }

    @Override
    public long countActiveByFormatId(Long formatId) {
        return editionJpaRepository.countActiveByFormatId(formatId);
    }

    @Override
    public long countActiveByLanguageId(Long languageId) {
        return editionJpaRepository.countActiveByLanguageId(languageId);
    }

    @Override
    public List<EditionWithNamesDTO> findByWorkIdWithDetails(Long workId) {
        return editionJpaRepository.findByWorkIdWithDetails(workId);
    }

    @Override
    public List<EditionAuthor> findEditionAuthorsByEditionId(Long editionId) {
        return editionAuthorJpaRepository.findByEditionId(editionId).stream()
                .map(EditionAuthorMapper::toDomain)
                .toList();
    }
}
