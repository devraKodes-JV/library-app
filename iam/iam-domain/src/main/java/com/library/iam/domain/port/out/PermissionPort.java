package com.library.iam.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.iam.domain.model.Permission;

/**
 * Output (driven) port for permission persistence.
 *
 * <p>Implemented by an adapter in the infrastructure layer. The domain and
 * application layers depend only on this interface. Permissions are seeded in
 * code (Flyway migrations) and are NOT managed at runtime - the admin only
 * assigns existing permissions to roles.</p>
 */
public interface PermissionPort {

    /**
     * Finds a permission by its code, if present.
     *
     * @param code the permission code (e.g. "books.read")
     * @return an {@link Optional} containing the domain {@link Permission},
     *         or empty
     */
Optional<Permission> findByCode(String code);

    /**
     * Finds a permission by its id, if present and not deleted.
     *
     * @param id the permission id
     * @return an {@link Optional} containing the domain {@link Permission},
     *         or empty
     */
    Optional<Permission> findById(Long id);

    /**
     * Lists all non-deleted permissions.
     *
     * @return the list of active permissions (never null)
     */
    List<Permission> findAll();

    /**
     * Persists a permission.
     *
     * @param permission the domain permission to save
     * @return the saved permission
     */
    Permission save(Permission permission);
}
