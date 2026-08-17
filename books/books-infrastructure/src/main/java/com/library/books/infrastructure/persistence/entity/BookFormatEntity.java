package com.library.books.infrastructure.persistence.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.envers.Audited;

/**
 * JPA entity representing a book format.
 */
@Entity
@Table(name = "book_formats")
@Audited
public class BookFormatEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 500)
    private String description;

    private boolean enabled = true;

    public BookFormatEntity(Long id, String code, String name, String description, boolean enabled) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
    }

    public BookFormatEntity() {
    }

    /**
     * Gets the entity identifier.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the entity identifier.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the format code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the format code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the format name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the format name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the format description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the format description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns true if the format is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the format is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
