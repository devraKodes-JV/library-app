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
@Table(name = "editions")
@Audited
public class EditionEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_id", nullable = false)
    private Long workId;

    @Column(name = "publisher_id")
    private Long publisherId;

    @Column(name = "format_id")
    private Long formatId;

    @Column(name = "language_id")
    private Long languageId;

    @Column(length = 20)
    private String isbn;

    private Integer pages;

    @Column(name = "publication_year")
    private Integer publicationYear;

    @Column(name = "edition_number", length = 50)
    private String editionNumber;

    @Column(nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "edition")
    private List<EditionAuthorEntity> editionAuthors = new ArrayList<>();

    public EditionEntity(Long id, Long workId, Long publisherId, Long formatId, Long languageId, String isbn, Integer pages, Integer publicationYear, String editionNumber, boolean enabled) {
        this.id = id;
        this.workId = workId;
        this.publisherId = publisherId;
        this.formatId = formatId;
        this.languageId = languageId;
        this.isbn = isbn;
        this.pages = pages;
        this.publicationYear = publicationYear;
        this.editionNumber = editionNumber;
        this.enabled = enabled;
    }

    public EditionEntity() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getWorkId() { return workId; }
    public void setWorkId(Long workId) { this.workId = workId; }
    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }
    public Long getFormatId() { return formatId; }
    public void setFormatId(Long formatId) { this.formatId = formatId; }
    public Long getLanguageId() { return languageId; }
    public void setLanguageId(Long languageId) { this.languageId = languageId; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }
    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    public String getEditionNumber() { return editionNumber; }
    public void setEditionNumber(String editionNumber) { this.editionNumber = editionNumber; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public List<EditionAuthorEntity> getEditionAuthors() { return editionAuthors; }
    public void setEditionAuthors(List<EditionAuthorEntity> editionAuthors) { this.editionAuthors = editionAuthors; }
}
