package com.library.kernel.jpa;

import java.util.Optional;

/**
 * Capability: locate an entity by its natural business name.
 *
 * <p>Aggregates whose unique business key is a name (e.g. a role named
 * {@code ADMIN}) compose this capability on top of the generic CRUD contract.</p>
 *
 * <p>Generic on {@code T} so any entity with a {@code name} column can reuse
 * it without creating a new interface.</p>
 *
 * @param <T> the JPA entity type handled by the repository
 */
public interface FindableByName<T> {

    /**
     * Finds an entity by its natural name, excluding logically-deleted rows.
     *
     * @param name the business name (e.g. {@code ADMIN})
     * @return an {@link Optional} containing the entity, or empty if it does
     *         not exist or was logically deleted
     */
    Optional<T> findByName(String name);
}
