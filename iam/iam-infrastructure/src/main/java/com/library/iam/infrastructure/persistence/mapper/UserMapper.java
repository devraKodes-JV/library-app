package com.library.iam.infrastructure.persistence.mapper;

import com.library.iam.domain.model.User;
import com.library.iam.infrastructure.persistence.entity.UserEntity;

/**
 * Mapper between the JPA entity {@link UserEntity} and the domain {@link User}.
 *
 * <p>This is the bridge between the infrastructure layer (where JPA lives)
 * and the pure domain model. It keeps the domain free of JPA annotations.</p>
 */
public final class UserMapper {

    private UserMapper() {
        // Utility class: no instantiation.
    }

    /**
     * Converts a JPA entity to a domain model, including its role.
     *
     * @param e the JPA entity (may be null)
     * @return the domain {@link User}, or null if the input was null
     */
public static User toDomain(UserEntity e) {
        if (e == null) {
            return null;
        }
        User user = new User(
                e.getId(),
                e.getUsername(),
                e.getPassword(),
                e.getFullName(),
                e.getEmail(),
                e.isEnabled(),
                RoleMapper.toDomain(e.getRole()),
                e.getFailedLoginAttempts(),
                e.getLockedUntil());
        user.setDeletedAt(e.getDeletedAt());
        return user;
    }

    /**
     * Converts a domain model to a JPA entity, including its role.
     *
     * @param u the domain {@link User} (may be null)
     * @return the JPA {@link UserEntity}, or null if the input was null
     */
    public static UserEntity toEntity(User u) {
        if (u == null) {
            return null;
        }
        UserEntity entity = new UserEntity(
                u.getId(),
                u.getUsername(),
                u.getPassword(),
                u.getFullName(),
                u.getEmail(),
                u.isEnabled(),
                RoleMapper.toEntity(u.getRole()));
        entity.setFailedLoginAttempts(u.getFailedLoginAttempts());
        entity.setLockedUntil(u.getLockedUntil());
        entity.setDeletedAt(u.getDeletedAt());
        return entity;
    }
}
