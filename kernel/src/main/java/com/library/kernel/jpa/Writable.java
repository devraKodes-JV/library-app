package com.library.kernel.jpa;

/**
 * Capability: write access to a repository (lifecycle).
 *
 * <p>This single-purpose (ISP-compliant) capability interface declares the
 * write operations of an aggregate's lifecycle: persisting an entity and
 * logically deleting it. It stays generic on {@code T} (entity type) and
 * {@code ID} (primary key type) so any feature can reuse it.</p>
 *
 * <p>Deletion is deliberately <em>logical</em> (soft delete): a row is marked
 * with a {@code deleted_at} timestamp instead of being physically removed.
 * This preserves audit history (Envers) and referential integrity. The generic
 * {@link Readable} queries always filter these rows out, so a deleted entity
 * never surfaces again.</p>
 *
 * @param <T>  the JPA entity type handled by the repository
 * @param <ID> the primary key type of the entity (typically {@link Long})
 */
public interface Writable<T, ID> {

    /**
     * Persists an entity (insert if it has no id, update otherwise).
     *
     * @param entity the entity to save
     * @return the saved entity (with the id populated on insert)
     */
    T save(T entity);

    /**
     * Logically deletes an entity by its primary key (marks {@code deleted_at}).
     *
     * @param id the primary key of the entity to delete
     */
    void deleteById(ID id);
}
