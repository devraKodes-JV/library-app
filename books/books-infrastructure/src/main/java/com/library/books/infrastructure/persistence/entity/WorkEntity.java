package com.library.books.infrastructure.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "works")
@Audited
public class WorkEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(length = 500)
    private String subtitle;

    @Column(name = "original_language_id")
    private Long originalLanguageId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(length = 2000)
    private String summary;

    @Column(nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "work")
    private List<WorkAuthorEntity> workAuthors = new ArrayList<>();

    public WorkEntity(Long id, String title, String subtitle, Long originalLanguageId, Long categoryId, String summary, boolean enabled) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.originalLanguageId = originalLanguageId;
        this.categoryId = categoryId;
        this.summary = summary;
        this.enabled = enabled;
    }

    public WorkEntity() {
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
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public List<WorkAuthorEntity> getWorkAuthors() { return workAuthors; }
    public void setWorkAuthors(List<WorkAuthorEntity> workAuthors) { this.workAuthors = workAuthors; }
}
