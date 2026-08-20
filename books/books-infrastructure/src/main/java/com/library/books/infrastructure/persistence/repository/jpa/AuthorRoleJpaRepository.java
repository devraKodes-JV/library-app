package com.library.books.infrastructure.persistence.repository.jpa;

import java.util.List;
import java.util.Optional;

import com.library.books.infrastructure.persistence.entity.AuthorRoleEntity;
import com.library.kernel.jpa.CrudRepository;

public interface AuthorRoleJpaRepository<T, ID> extends CrudRepository<T, ID> {
    Optional<AuthorRoleEntity> findByCode(String code);
    Optional<AuthorRoleEntity> findByName(String name);
}
