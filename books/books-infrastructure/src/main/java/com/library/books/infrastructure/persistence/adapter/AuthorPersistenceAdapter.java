package com.library.books.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.library.books.domain.model.Author;
import com.library.books.domain.port.out.AuthorRepository;
import com.library.books.infrastructure.persistence.entity.AuthorEntity;
import com.library.books.infrastructure.persistence.mapper.AuthorMapper;
import com.library.books.infrastructure.persistence.repository.jpa.AuthorJpaRepository;
import com.library.books.domain.dto.common.WorkSummaryDTO;
import com.library.books.domain.dto.query.AuthorWithWorksDTO;

public class AuthorPersistenceAdapter implements AuthorRepository {

    private final AuthorJpaRepository<AuthorEntity, Long> authorJpaRepository;
    private final com.library.books.infrastructure.persistence.repository.hibernate.HibernateAuthorRepository hibernateAuthorRepository;

    public AuthorPersistenceAdapter(AuthorJpaRepository<AuthorEntity, Long> authorJpaRepository, com.library.books.infrastructure.persistence.repository.hibernate.HibernateAuthorRepository hibernateAuthorRepository) {
        this.authorJpaRepository = authorJpaRepository;
        this.hibernateAuthorRepository = hibernateAuthorRepository;
    }

    @Override
    public Optional<Author> findById(Long id) {
        return authorJpaRepository.findById(id)
                .map(AuthorMapper::toDomain);
    }

    @Override
    public List<Author> findAll() {
        return authorJpaRepository.findAll().stream()
                .map(AuthorMapper::toDomain)
                .toList();
    }

    @Override
    public Author save(Author author) {
        AuthorEntity entity = AuthorMapper.toEntity(author);
        AuthorEntity saved = authorJpaRepository.save(entity);
        return AuthorMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        authorJpaRepository.deleteById(id);
    }

    @Override
    public Map<Long, String> findNamesByIds(List<Long> ids) {
        return authorJpaRepository.findNamesByIds(ids);
    }

    @Override
    public Map<Long, String> findFullNamesByIds(List<Long> ids) {
        return authorJpaRepository.findFullNamesByIds(ids);
    }

    @Override
    public AuthorWithWorksDTO findByIdWithWorks(Long id) {
        return hibernateAuthorRepository.findByIdWithWorks(id);
    }
}
