package com.library.books.domain.model;

import java.time.Instant;
import java.util.List;

public class Edition {
    private Long id;
    private Long workId;
    private Long publisherId;
    private Long formatId;
    private Long languageId;
    private String isbn;
    private Integer pages;
    private Integer publicationYear;
    private String editionNumber;
    private Instant createdAt;
    private Instant updatedAt;
    private List<EditionAuthor> editionAuthors;

    public Edition(Long id, Long workId, Long publisherId, Long formatId, Long languageId, String isbn, Integer pages, Integer publicationYear, String editionNumber, Instant createdAt, Instant updatedAt, List<EditionAuthor> editionAuthors) {
        this.id = id;
        this.workId = workId;
        this.publisherId = publisherId;
        this.formatId = formatId;
        this.languageId = languageId;
        this.isbn = isbn;
        this.pages = pages;
        this.publicationYear = publicationYear;
        this.editionNumber = editionNumber;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.editionAuthors = editionAuthors;
    }

    public static Edition withoutId(Long workId, Long publisherId, Long formatId, Long languageId, String isbn, Integer pages, Integer publicationYear, String editionNumber) {
        return new Edition(null, workId, publisherId, formatId, languageId, isbn, pages, publicationYear, editionNumber, null, null, null);
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
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
    public List<EditionAuthor> getEditionAuthors() { return editionAuthors; }
    public void setEditionAuthors(List<EditionAuthor> editionAuthors) { this.editionAuthors = editionAuthors; }
}