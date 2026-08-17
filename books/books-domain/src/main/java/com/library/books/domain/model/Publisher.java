package com.library.books.domain.model;

import java.time.Instant;

public class Publisher {
    private Long id;
    private String name;
    private String country;
    private String website;
    private Instant createdAt;
    private Instant updatedAt;

    public Publisher(Long id, String name, String country, String website, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.website = website;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Publisher withoutId(String name, String country, String website) {
        return new Publisher(null, name, country, website, null, null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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