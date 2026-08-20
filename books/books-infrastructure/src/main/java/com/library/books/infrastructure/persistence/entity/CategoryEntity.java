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
 * JPA entity representing a category.
 */
@Entity
@Table(name = "categories")
@Audited
public class CategoryEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "parent_id")
    private Long parentId;

    private boolean enabled = true;

    public CategoryEntity(Long id, String code, String name, String description, Long parentId, boolean enabled) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.enabled = enabled;
    }

    public CategoryEntity() {
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
     * Gets the category code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the category code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the category name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the category name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the category description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the category description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the parent category identifier.
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * Sets the parent category identifier.
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * Returns true if the category is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the category is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
