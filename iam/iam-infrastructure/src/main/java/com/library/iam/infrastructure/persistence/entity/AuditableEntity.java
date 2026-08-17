package com.library.iam.infrastructure.persistence.entity;

import java.time.Instant;

import com.library.iam.infrastructure.security.CurrentUser;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * Base class for all JPA entities that need audit metadata.
 *
* <p>Every auditable entity gains the following columns:</p>
 * <ul>
 *   <li>{@code created_at} / {@code updated_at} — timestamps (UTC instant)</li>
 *   <li>{@code created_by} / {@code updated_by} — username of the acting user</li>
 *   <li>{@code deleted_at} — logical delete timestamp (null = active)</li>
 *   <li>{@code deleted_by} — username of the user who logically deleted the row</li>
 * </ul>
 *
 * <p>The {@link #onPersist()} and {@link #onUpdate()} callbacks are invoked by
 * JPA before insert/update and fill the audit fields automatically. The acting
 * user is read from {@link CurrentUser}'s {@code ScopedValue}, which is bound by
 * the authentication filter for each HTTP request. This is safe with virtual
 * threads and provides automatic cleanup without manual intervention.</p>
 */
@MappedSuperclass
public abstract class AuditableEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

@Column(name = "deleted_at")
    private Instant deletedAt;

    @Column(name = "deleted_by", length = 50)
    private String deletedBy;

    @PrePersist
    protected void onPersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.createdBy == null) {
            this.createdBy = CurrentUser.get();
        }
        this.updatedBy = CurrentUser.get();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = CurrentUser.get();
    }

    // ------------------------------------------------------------------
    // Getters / setters
    // ------------------------------------------------------------------

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    /** Convenience: is this entity actively visible (not logically deleted)? */
    public boolean isActive() {
        return deletedAt == null;
    }

    /**
     * Marks this entity as logically deleted, stamping both the timestamp and
     * the acting user (from {@link CurrentUser}). This is the single place
     * that performs a soft-delete so every entity writes {@code deleted_by}
     * consistently.
     */
    public void markDeleted() {
        this.deletedAt = Instant.now();
        this.deletedBy = CurrentUser.get();
    }
}
