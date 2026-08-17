package com.library.kernel.jpa;

import java.util.Optional;

/**
 * Capability: locate an entity by its natural business code.
 *
 * <p>A "natural key" differs from the surrogate primary key: it is a stable,
 * human-readable unique value such as a permission code ({@code books.read})
 * or a module code ({@code iam}). Aggregates that expose such a code compose
 * this capability on top of the generic CRUD contract.</p>
 *
 * <p>Generic on {@code T} so any entity with a {@code code} column can reuse
 * it without creating a new interface.</p>
 *
 * @param <T> the JPA entity type handled by the repository
 */
public interface FindableByCode<T> {

    /**
     * Finds an entity by its natural code, excluding logically-deleted rows.
     *
     * @param code the business code (e.g. {@code iam}, {@code books.read})
     * @return an {@link Optional} containing the entity, or empty if it does
     *         not exist or was logically deleted
     */
    Optional<T> findByCode(String code);
}
