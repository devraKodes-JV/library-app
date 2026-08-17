package com.library.iam.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.iam.domain.model.Role;
import com.library.iam.domain.port.out.RolePort;
import com.library.iam.infrastructure.persistence.entity.RoleEntity;
import com.library.iam.infrastructure.persistence.mapper.RoleMapper;
import com.library.iam.infrastructure.persistence.repository.jpa.RoleJpaRepository;

/**
 * Persistence adapter that implements the domain {@link RolePort}.
 *
 * <p>Converts domain {@link Role} objects to JPA entities and back, using
 * {@link RoleMapper} and {@link RoleJpaRepository}.</p>
 */
public class RolePersistenceAdapter implements RolePort {

    private final RoleJpaRepository<RoleEntity, Long> roleJpaRepository;

    public RolePersistenceAdapter(RoleJpaRepository<RoleEntity, Long> roleJpaRepository) {
        this.roleJpaRepository = roleJpaRepository;
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name)
                .map(RoleMapper::toDomain);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id)
                .map(RoleMapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream()
                .map(RoleMapper::toDomain)
                .toList();
    }

    @Override
    public Role save(Role role) {
        RoleEntity entity = RoleMapper.toEntity(role);
        RoleEntity saved = roleJpaRepository.save(entity);
        return RoleMapper.toDomain(saved);
    }

    @Override
    public void delete(Long id) {
        roleJpaRepository.deleteById(id);
    }
}
