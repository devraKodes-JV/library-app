package com.library.books.infrastructure.persistence.entity;

import java.io.Serializable;
import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public abstract class AuditableEntity implements Serializable {

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
    void onCreate() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.createdBy = resolveCurrentUser();
        this.updatedBy = resolveCurrentUser();
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = resolveCurrentUser();
    }

    private String resolveCurrentUser() {
        return com.library.iam.infrastructure.security.CurrentUser.getOrDefault("system");
    }

    /**
     * Returns true if the entity is not soft-deleted.
     */
    public boolean isActive() {
        return deletedAt == null;
    }

    /**
     * Soft-deletes the entity.
     */
    public void markDeleted() {
        this.deletedAt = Instant.now();
        this.deletedBy = resolveCurrentUser();
    }

    /**
     * Gets the creation timestamp.
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     */
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Gets the last update timestamp.
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last update timestamp.
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the user who created the entity.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created the entity.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets the user who last updated the entity.
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets the user who last updated the entity.
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * Gets the deletion timestamp, or null if not deleted.
     */
    public Instant getDeletedAt() {
        return deletedAt;
    }

    /**
     * Sets the deletion timestamp.
     */
    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * Gets the user who deleted the entity, or null if not deleted.
     */
    public String getDeletedBy() {
        return deletedBy;
    }

    /**
     * Sets the user who deleted the entity.
     */
    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    /**
     * JPA entity listener for audit fields.
     */
    public static class AuditListener {
    }
}
