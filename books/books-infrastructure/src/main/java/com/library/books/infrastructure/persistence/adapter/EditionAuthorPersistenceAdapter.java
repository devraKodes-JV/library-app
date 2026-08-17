package com.library.books.infrastructure.persistence.adapter;

import java.util.List;

import com.library.books.domain.model.EditionAuthor;
import com.library.books.domain.port.out.EditionAuthorRepository;
import com.library.books.infrastructure.persistence.entity.EditionAuthorEntity;
import com.library.books.infrastructure.persistence.mapper.EditionAuthorMapper;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateEditionAuthorRepository;

public class EditionAuthorPersistenceAdapter implements EditionAuthorRepository {

    private final HibernateEditionAuthorRepository editionAuthorRepository;

    public EditionAuthorPersistenceAdapter(HibernateEditionAuthorRepository editionAuthorRepository) {
        this.editionAuthorRepository = editionAuthorRepository;
    }

    @Override
    public EditionAuthor save(EditionAuthor editionAuthor) {
        EditionAuthorEntity entity = EditionAuthorMapper.toEntity(editionAuthor);
        EditionAuthorEntity saved = editionAuthorRepository.save(entity);
        return EditionAuthorMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        editionAuthorRepository.deleteById(id);
    }

    @Override
    public List<EditionAuthor> findByEditionId(Long editionId) {
        return editionAuthorRepository.findByEditionId(editionId).stream()
                .map(EditionAuthorMapper::toDomain)
                .toList();
    }

    @Override
    public List<EditionAuthor> findByAuthorId(Long authorId) {
        return editionAuthorRepository.findByAuthorId(authorId).stream()
                .map(EditionAuthorMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByEditionId(Long editionId) {
        editionAuthorRepository.deleteByEditionId(editionId);
    }

    @Override
    public void deleteByAuthorId(Long authorId) {
        editionAuthorRepository.deleteByAuthorId(authorId);
    }
}
