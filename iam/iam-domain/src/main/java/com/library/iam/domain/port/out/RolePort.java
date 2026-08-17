package com.library.iam.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.iam.domain.model.Role;

/**
 * Output (driven) port for role persistence.
 *
 * <p>Implemented by an adapter in the infrastructure layer. The domain and
 * application layers depend only on this interface.</p>
 */
public interface RolePort {

    /**
     * Finds a role by its name, if present.
     *
     * @param name the role name (ADMIN, EMPLOYEE, CLIENT)
     * @return an {@link Optional} containing the domain {@link Role}, or empty
     */
    Optional<Role> findByName(String name);

    /**
     * Finds a role by its id, if present.
     *
     * @param id the role id
     * @return an {@link Optional} containing the domain {@link Role}, or empty
     */
    Optional<Role> findById(Long id);

    /**
     * Lists all non-deleted roles.
     *
     * @return the list of active roles (never null)
     */
    List<Role> findAll();

    /**
     * Persists a role.
     *
     * @param role the domain role to save
     * @return the saved role
     */
    Role save(Role role);

    /**
     * Logically deletes a role by marking its {@code deleted_at} timestamp.
     *
     * @param id the role id to delete
     */
    void delete(Long id);
}
