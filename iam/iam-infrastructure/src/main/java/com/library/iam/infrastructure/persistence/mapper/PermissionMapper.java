package com.library.iam.infrastructure.persistence.mapper;

import com.library.iam.domain.model.Permission;
import com.library.iam.infrastructure.persistence.entity.PermissionEntity;

/**
 * Mapper between the JPA entity {@link PermissionEntity} and the domain
 * {@link Permission}.
 */
public final class PermissionMapper {

    private PermissionMapper() {
        // Utility class: no instantiation.
    }

    /**
     * Converts a JPA entity to a domain model, including its module.
     *
     * @param e the JPA entity (may be null)
     * @return the domain {@link Permission}, or null if the input was null
     */
    public static Permission toDomain(PermissionEntity e) {
        if (e == null) {
            return null;
        }
Permission p = new Permission(
                e.getId(),
                e.getCode(),
                e.getName(),
                e.getMenuLabel(),
                e.getIcon(),
                e.getUrl(),
                e.getSortOrder(),
                ModuleMapper.toDomain(e.getModule()));
        p.setEnabled(e.isEnabled());
        return p;
    }

    /**
     * Converts a domain model to a JPA entity, including its module.
     *
     * @param p the domain {@link Permission} (may be null)
     * @return the JPA {@link PermissionEntity}, or null if the input was null
     */
    public static PermissionEntity toEntity(Permission p) {
        if (p == null) {
            return null;
        }
        PermissionEntity e = new PermissionEntity();
        e.setId(p.getId());
        e.setCode(p.getCode());
        e.setName(p.getName());
        e.setMenuLabel(p.getMenuLabel());
        e.setIcon(p.getIcon());
        e.setUrl(p.getUrl());
        e.setSortOrder(p.getSortOrder());
        e.setEnabled(p.isEnabled());
        e.setModule(ModuleMapper.toEntity(p.getModule()));
        return e;
    }
}
