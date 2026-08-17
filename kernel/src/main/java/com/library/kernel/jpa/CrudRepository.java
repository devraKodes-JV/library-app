package com.library.kernel.jpa;

/**
 * Composition of the basic CRUD capabilities (read + write).
 *
 * <p>This interface composes {@link Readable} and {@link Writable} into a
 * single, reusable "CRUD" capability. It stays generic on both {@code T} and
 * {@code ID} so it can be reused by any aggregate without being tied to a
 * concrete entity type.</p>
 *
 * <p>Concrete per-aggregate repositories extend this (usually together with one
 * or more natural-key finders) but remain generic; only the concrete Hibernate
 * implementation fixes the entity types. See the feature repositories in
 * {@code iam-infrastructure}.</p>
 *
 * @param <T>  the JPA entity type handled by the repository
 * @param <ID> the primary key type of the entity (typically {@link Long})
 */
public interface CrudRepository<T, ID> extends Readable<T, ID>, Writable<T, ID> {
    // Intentionally empty: it is a semantic composition of the two capabilities.
}
