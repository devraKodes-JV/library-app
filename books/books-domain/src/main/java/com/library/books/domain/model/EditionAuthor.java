package com.library.books.domain.model;

import java.time.Instant;

public class EditionAuthor {
    private Long id;
    private Long editionId;
    private Long authorId;
    private Long authorRoleId;
    private Instant createdAt;
    private Instant updatedAt;

    public EditionAuthor(Long id, Long editionId, Long authorId, Long authorRoleId, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.editionId = editionId;
        this.authorId = authorId;
        this.authorRoleId = authorRoleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static EditionAuthor withoutId(Long editionId, Long authorId, Long authorRoleId) {
        return new EditionAuthor(null, editionId, authorId, authorRoleId, null, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEditionId() {
        return editionId;
    }

    public void setEditionId(Long editionId) {
        this.editionId = editionId;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getAuthorRoleId() { return authorRoleId; }
    public void setAuthorRoleId(Long authorRoleId) { this.authorRoleId = authorRoleId; }

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
}
