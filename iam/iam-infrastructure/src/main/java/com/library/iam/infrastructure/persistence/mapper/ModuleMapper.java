package com.library.iam.infrastructure.persistence.mapper;

import com.library.iam.domain.model.Module;
import com.library.iam.infrastructure.persistence.entity.ModuleEntity;

/**
 * Mapper between the JPA entity {@link ModuleEntity} and the domain
 * {@link Module}.
 */
public final class ModuleMapper {

    private ModuleMapper() {
        // Utility class: no instantiation.
    }

    /**
     * Converts a JPA entity to a domain model.
     *
     * @param e the JPA entity (may be null)
     * @return the domain {@link Module}, or null if the input was null
     */
    public static Module toDomain(ModuleEntity e) {
        if (e == null) {
            return null;
        }
        return new Module(
                e.getId(),
                e.getCode(),
                e.getName(),
                e.getMenuLabel(),
                e.getIcon(),
                e.getSortOrder(),
                e.isEnabled());
    }

    /**
     * Converts a domain model to a JPA entity.
     *
     * @param m the domain {@link Module} (may be null)
     * @return the JPA {@link ModuleEntity}, or null if the input was null
     */
    public static ModuleEntity toEntity(Module m) {
        if (m == null) {
            return null;
        }
        return new ModuleEntity(
                m.getId(),
                m.getCode(),
                m.getName(),
                m.getMenuLabel(),
                m.getIcon(),
                m.getSortOrder(),
                m.isEnabled());
    }
}
