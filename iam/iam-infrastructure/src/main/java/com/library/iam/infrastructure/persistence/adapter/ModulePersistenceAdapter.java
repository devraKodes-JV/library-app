package com.library.iam.infrastructure.persistence.adapter;

import java.util.List;
import java.util.Optional;

import com.library.iam.domain.model.Module;
import com.library.iam.domain.port.out.ModulePort;
import com.library.iam.infrastructure.persistence.entity.ModuleEntity;
import com.library.iam.infrastructure.persistence.mapper.ModuleMapper;
import com.library.iam.infrastructure.persistence.repository.jpa.ModuleJpaRepository;

/**
 * Persistence adapter that implements the domain {@link ModulePort}.
 *
 * <p>Converts domain {@link Module} objects to JPA entities and back, using
 * {@link ModuleMapper} and {@link ModuleJpaRepository}.</p>
 */
public class ModulePersistenceAdapter implements ModulePort {

    private final ModuleJpaRepository<ModuleEntity, Long> moduleJpaRepository;

    public ModulePersistenceAdapter(ModuleJpaRepository<ModuleEntity, Long> moduleJpaRepository) {
        this.moduleJpaRepository = moduleJpaRepository;
    }

    @Override
    public Optional<Module> findByCode(String code) {
        return moduleJpaRepository.findByCode(code)
                .map(ModuleMapper::toDomain);
    }

    @Override
    public List<Module> findAll() {
        return moduleJpaRepository.findAll().stream()
                .map(ModuleMapper::toDomain)
                .toList();
    }

    @Override
    public Module save(Module module) {
        ModuleEntity entity = ModuleMapper.toEntity(module);
        ModuleEntity saved = moduleJpaRepository.save(entity);
        return ModuleMapper.toDomain(saved);
    }
}
