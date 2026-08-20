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
@Table(name = "edition_authors")
@Audited
public class EditionAuthorEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "edition_id", nullable = false, insertable = false, updatable = false)
    private Long editionId;

    @Column(name = "author_id", nullable = false, insertable = false, updatable = false)
    private Long authorId;

    @Column(name = "author_role_id", nullable = false, insertable = false, updatable = false)
    private Long authorRoleId;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "edition_id", insertable = false, updatable = false)
    private EditionEntity edition;

    @ManyToOne
    @JoinColumn(name = "author_role_id", insertable = false, updatable = false)
    private AuthorRoleEntity authorRole;

    public EditionAuthorEntity(Long id, Long editionId, Long authorId, Long authorRoleId, boolean enabled) {
        this.id = id;
        this.editionId = editionId;
        this.authorId = authorId;
        this.authorRoleId = authorRoleId;
        this.enabled = enabled;
    }

    public EditionAuthorEntity() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getEditionId() { return editionId; }
    public void setEditionId(Long editionId) { this.editionId = editionId; }
    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public Long getAuthorRoleId() { return authorRoleId; }
    public void setAuthorRoleId(Long authorRoleId) { this.authorRoleId = authorRoleId; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public EditionEntity getEdition() { return edition; }
    public void setEdition(EditionEntity edition) { this.edition = edition; }
    public AuthorRoleEntity getAuthorRole() { return authorRole; }
    public void setAuthorRole(AuthorRoleEntity authorRole) { this.authorRole = authorRole; }
}
