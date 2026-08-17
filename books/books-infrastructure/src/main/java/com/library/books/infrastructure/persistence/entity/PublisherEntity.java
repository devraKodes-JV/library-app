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
 * JPA entity representing a publisher.
 */
@Entity
@Table(name = "publishers")
@Audited
public class PublisherEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 200)
    private String name;

    @Column(length = 100)
    private String country;

    @Column(length = 300)
    private String website;

    private boolean enabled = true;

    public PublisherEntity(Long id, String name, String country, String website, boolean enabled) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.website = website;
        this.enabled = enabled;
    }

    public PublisherEntity() {
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
     * Gets the publisher name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the publisher name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the publisher country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the publisher country.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets the publisher website.
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Sets the publisher website.
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Returns true if the publisher is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the publisher is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
