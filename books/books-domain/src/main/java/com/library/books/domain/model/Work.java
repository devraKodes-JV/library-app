package com.library.books.domain.model;

import java.time.Instant;
import java.util.List;

public class Work {
    private Long id;
    private String title;
    private String subtitle;
    private Long originalLanguageId;
    private Long categoryId;
    private String summary;
    private Instant createdAt;
    private Instant updatedAt;
    private List<WorkAuthor> workAuthors;

    public Work(Long id, String title, String subtitle, Long originalLanguageId, Long categoryId, String summary, Instant createdAt, Instant updatedAt, List<WorkAuthor> workAuthors) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.originalLanguageId = originalLanguageId;
        this.categoryId = categoryId;
        this.summary = summary;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.workAuthors = workAuthors;
    }

    public static Work withoutId(String title, String subtitle, Long originalLanguageId, Long categoryId, String summary) {
        return new Work(null, title, subtitle, originalLanguageId, categoryId, summary, null, null, null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public Long getOriginalLanguageId() { return originalLanguageId; }
    public void setOriginalLanguageId(Long originalLanguageId) { this.originalLanguageId = originalLanguageId; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<WorkAuthor> getWorkAuthors() { return workAuthors; }
    public void setWorkAuthors(List<WorkAuthor> workAuthors) { this.workAuthors = workAuthors; }
}