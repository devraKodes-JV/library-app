package com.library.books.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.books.domain.model.AuthorRole;
import com.library.books.domain.port.out.AuthorRoleRepository;
import com.library.books.infrastructure.persistence.entity.AuthorRoleEntity;
import com.library.books.infrastructure.persistence.mapper.AuthorRoleMapper;
import com.library.books.infrastructure.persistence.repository.hibernate.HibernateAuthorRoleRepository;

public class AuthorRolePersistenceAdapter implements AuthorRoleRepository {

    private final HibernateAuthorRoleRepository authorRoleRepository;

    public AuthorRolePersistenceAdapter(HibernateAuthorRoleRepository authorRoleRepository) {
        this.authorRoleRepository = authorRoleRepository;
    }

    @Override
    public List<AuthorRole> findAll() {
        return authorRoleRepository.findAll().stream()
                .map(AuthorRoleMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<AuthorRole> findById(Long id) {
        return authorRoleRepository.findById(id)
                .map(AuthorRoleMapper::toDomain);
    }

    @Override
    public Optional<AuthorRole> findByCode(String code) {
        return authorRoleRepository.findByCode(code)
                .map(AuthorRoleMapper::toDomain);
    }

    @Override
    public Optional<AuthorRole> findByName(String name) {
        return authorRoleRepository.findByName(name)
                .map(AuthorRoleMapper::toDomain);
    }

    @Override
    public AuthorRole save(AuthorRole authorRole) {
        AuthorRoleEntity entity = AuthorRoleMapper.toEntity(authorRole);
        AuthorRoleEntity saved = authorRoleRepository.save(entity);
        return AuthorRoleMapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        authorRoleRepository.deleteById(id);
    }
}
