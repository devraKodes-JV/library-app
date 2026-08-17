package com.library.books.domain.model;

import java.time.Instant;

public class WorkAuthor {
    private Long id;
    private Long workId;
    private Long authorId;
    private String role;
    private Instant createdAt;
    private Instant updatedAt;

    public WorkAuthor(Long id, Long workId, Long authorId, String role, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.workId = workId;
        this.authorId = authorId;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static WorkAuthor withoutId(Long workId, Long authorId, String role) {
        return new WorkAuthor(null, workId, authorId, role, null, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
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
