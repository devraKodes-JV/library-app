package com.library.books.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.Edition;
import com.library.books.domain.port.out.EditionRepository;
import com.library.books.infrastructure.persistence.entity.EditionEntity;
import com.library.books.infrastructure.persistence.mapper.EditionMapper;
import com.library.books.infrastructure.persistence.repository.jpa.EditionJpaRepository;

public class EditionPersistenceAdapter implements EditionRepository {

    private final EditionJpaRepository<EditionEntity, Long> editionJpaRepository;

    public EditionPersistenceAdapter(EditionJpaRepository<EditionEntity, Long> editionJpaRepository) {
        this.editionJpaRepository = editionJpaRepository;
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
}
