package com.library.books.domain.model;

import java.time.Instant;

public class EditionAuthor {
    private Long id;
    private Long editionId;
    private Long authorId;
    private String role;
    private Instant createdAt;
    private Instant updatedAt;

    public EditionAuthor(Long id, Long editionId, Long authorId, String role, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.editionId = editionId;
        this.authorId = authorId;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static EditionAuthor withoutId(Long editionId, Long authorId, String role) {
        return new EditionAuthor(null, editionId, authorId, role, null, null);
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

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
