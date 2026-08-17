package com.library.iam.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.iam.domain.model.Permission;
import com.library.iam.domain.port.out.PermissionPort;
import com.library.iam.infrastructure.persistence.entity.PermissionEntity;
import com.library.iam.infrastructure.persistence.mapper.PermissionMapper;
import com.library.iam.infrastructure.persistence.repository.jpa.PermissionJpaRepository;

/**
 * Persistence adapter that implements the domain {@link PermissionPort}.
 *
 * <p>Converts domain {@link Permission} objects to JPA entities and back,
 * using {@link PermissionMapper} and {@link PermissionJpaRepository}.</p>
 */
public class PermissionPersistenceAdapter implements PermissionPort {

    private final PermissionJpaRepository<PermissionEntity, Long> permissionJpaRepository;

    public PermissionPersistenceAdapter(PermissionJpaRepository<PermissionEntity, Long> permissionJpaRepository) {
        this.permissionJpaRepository = permissionJpaRepository;
    }

    @Override
    public Optional<Permission> findByCode(String code) {
        return permissionJpaRepository.findByCode(code)
                .map(PermissionMapper::toDomain);
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionJpaRepository.findById(id)
                .map(PermissionMapper::toDomain);
    }

    @Override
    public List<Permission> findAll() {
        return permissionJpaRepository.findAll().stream()
                .map(PermissionMapper::toDomain)
                .toList();
    }

    @Override
    public Permission save(Permission permission) {
        PermissionEntity entity = PermissionMapper.toEntity(permission);
        PermissionEntity saved = permissionJpaRepository.save(entity);
        return PermissionMapper.toDomain(saved);
    }
}
