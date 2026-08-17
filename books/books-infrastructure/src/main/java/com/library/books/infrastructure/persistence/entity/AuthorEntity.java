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

/**
 * JPA entity representing an author.
 */
@Entity
@Table(name = "authors")
@Audited
public class AuthorEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 2000)
    private String biography;

    @Column(name = "birth_date", length = 20)
    private String birthDate;

    @Column(name = "death_date", length = 20)
    private String deathDate;

    private boolean enabled = true;

    @OneToMany(mappedBy = "author")
    private List<WorkAuthorEntity> workAuthors = new ArrayList<>();

    public AuthorEntity(Long id, String firstName, String lastName, String biography, String birthDate, String deathDate, boolean enabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.biography = biography;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.enabled = enabled;
    }

    public AuthorEntity() {
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
     * Gets the author first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the author first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the author last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the author last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the author biography.
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Sets the author biography.
     */
    public void setBiography(String biography) {
        this.biography = biography;
    }

    /**
     * Gets the author birth date.
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the author birth date.
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Gets the author death date.
     */
    public String getDeathDate() {
        return deathDate;
    }

    /**
     * Sets the author death date.
     */
    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    /**
     * Returns true if the author is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether the author is enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<WorkAuthorEntity> getWorkAuthors() {
        return workAuthors;
    }

    public void setWorkAuthors(List<WorkAuthorEntity> workAuthors) {
        this.workAuthors = workAuthors;
    }
}
