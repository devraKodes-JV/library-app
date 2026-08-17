package com.library.iam.infrastructure.persistence.mapper;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.library.iam.domain.model.Permission;
import com.library.iam.domain.model.Role;
import com.library.iam.infrastructure.persistence.entity.PermissionEntity;
import com.library.iam.infrastructure.persistence.entity.RoleEntity;

/**
 * Mapper between the JPA entity {@link RoleEntity} and the domain {@link Role}.
 */
public final class RoleMapper {

    private RoleMapper() {
        // Utility class: no instantiation.
    }

    /**
     * Converts a JPA entity to a domain model, including its permissions.
     *
     * @param e the JPA entity (may be null)
     * @return the domain {@link Role}, or null if the input was null
     */
    public static Role toDomain(RoleEntity e) {
        if (e == null) {
            return null;
        }
        Role role = new Role(e.getId(), e.getName(), e.getDescription());
        role.setEnabled(e.isEnabled());
        Set<Permission> permissions = new LinkedHashSet<>();
        for (PermissionEntity pe : e.getPermissions()) {
            permissions.add(PermissionMapper.toDomain(pe));
        }
        role.setPermissions(permissions);
        return role;
    }

    /**
     * Converts a domain model to a JPA entity, including its permissions.
     *
     * @param r the domain {@link Role} (may be null)
     * @return the JPA {@link RoleEntity}, or null if the input was null
     */
    public static RoleEntity toEntity(Role r) {
        if (r == null) {
            return null;
        }
RoleEntity e = new RoleEntity(r.getId(), r.getName(), r.getDescription(), r.isEnabled());
        if (r.getPermissions() != null) {
            e.setPermissions(r.getPermissions().stream()
                    .map(PermissionMapper::toEntity)
                    .collect(Collectors.toCollection(LinkedHashSet::new)));
        }
        return e;
    }
}
