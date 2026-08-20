package com.library.books.infrastructure.persistence.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "work_authors")
@Audited
public class WorkAuthorEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_id", nullable = false, insertable = false, updatable = false)
    private Long workId;

    @Column(name = "author_id", nullable = false, insertable = false, updatable = false)
    private Long authorId;

    @Column(name = "author_role_id", nullable = false, insertable = false, updatable = false)
    private Long authorRoleId;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToOne
    private AuthorEntity author;

    @ManyToOne
    @JoinColumn(name = "work_id", insertable = false, updatable = false)
    private WorkEntity work;

    @ManyToOne
    @JoinColumn(name = "author_role_id", insertable = false, updatable = false)
    private AuthorRoleEntity authorRole;

    public WorkAuthorEntity(Long id, Long workId, Long authorId, Long authorRoleId, boolean enabled) {
        this.id = id;
        this.workId = workId;
        this.authorId = authorId;
        this.authorRoleId = authorRoleId;
        this.enabled = enabled;
    }

    public WorkAuthorEntity() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWorkId() { return workId; }
    public void setWorkId(Long workId) { this.workId = workId; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public Long getAuthorRoleId() { return authorRoleId; }
    public void setAuthorRoleId(Long authorRoleId) { this.authorRoleId = authorRoleId; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public AuthorEntity getAuthor() { return author; }
    public void setAuthor(AuthorEntity author) { this.author = author; }
    public WorkEntity getWork() { return work; }
    public void setWork(WorkEntity work) { this.work = work; }
    public AuthorRoleEntity getAuthorRole() { return authorRole; }
    public void setAuthorRole(AuthorRoleEntity authorRole) { this.authorRole = authorRole; }
}
