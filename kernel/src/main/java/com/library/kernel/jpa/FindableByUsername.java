package com.library.kernel.jpa;

import java.util.Optional;

/**
 * Capability: locate an entity by its natural username key.
 *
 * <p>The {@code user} aggregate exposes a unique username. This capability
 * declares that lookup so the authentication layer can load a user by login
 * name without coupling to Hibernate or a concrete model.</p>
 *
 * <p>Generic on {@code T} so any entity with a {@code username} column can
 * reuse it.</p>
 *
 * @param <T> the JPA entity type handled by the repository
 */
public interface FindableByUsername<T> {

    /**
     * Finds an entity by its username, excluding logically-deleted rows.
     *
     * @param username the login name (e.g. {@code admin})
     * @return an {@link Optional} containing the entity, or empty if it does
     *         not exist or was logically deleted
     */
    Optional<T> findByUsername(String username);
}
