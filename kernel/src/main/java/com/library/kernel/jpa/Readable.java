package com.library.kernel.jpa;

import java.util.List;
import java.util.Optional;

/**
 * Capability: read access to a repository.
 *
 * <p>This is a single-purpose (ISP-compliant) capability interface. It declares
 * the two fundamental read operations any persistent aggregate needs: fetching
 * a single entity by its primary key and listing all non-deleted entities.</p>
 *
 * <p>It is deliberately generic on both {@code T} (the entity type) and
 * {@code ID} (the primary key type) so it can be reused by any feature without
 * being coupled to a concrete model. Implementations must filter out
 * logically-deleted rows so deleted data never reaches the application layer.</p>
 *
 * @param <T>  the JPA entity type handled by the repository
 * @param <ID> the primary key type of the entity (typically {@link Long})
 */
public interface Readable<T, ID> {

    /**
     * Finds a non-deleted entity by its primary key.
     *
     * @param id the primary key
     * @return an {@link Optional} containing the entity, or empty if it does
     *         not exist or was logically deleted
     */
    Optional<T> findById(ID id);

    /**
     * Lists all non-deleted entities.
     *
     * @return the list of active entities (never null); may be empty
     */
    List<T> findAll();
}
