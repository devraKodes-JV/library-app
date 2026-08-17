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
 * JPA entity representing a language.
 */
@Entity
@Table(name = "languages")
@Audited
public class LanguageEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    private boolean enabled = true;

    public LanguageEntity(Long id, String code, String name, boolean enabled) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.enabled = enabled;
    }

    public LanguageEntity() {
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
     * Gets the language code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the language code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the language name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the language name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns true if the language is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the language is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
