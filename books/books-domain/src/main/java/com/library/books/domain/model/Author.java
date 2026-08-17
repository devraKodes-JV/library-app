package com.library.books.domain.model;

import java.time.Instant;

public class Author {
    private Long id;
    private String firstName;
    private String lastName;
    private String biography;
    private String birthDate;
    private String deathDate;
    private Instant createdAt;
    private Instant updatedAt;

    public Author(Long id, String firstName, String lastName, String biography, String birthDate, String deathDate, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.biography = biography;
        this.birthDate = birthDate;
        this.deathDate = deathDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Author withoutId(String firstName, String lastName, String biography, String birthDate, String deathDate) {
        return new Author(null, firstName, lastName, biography, birthDate, deathDate, null, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
