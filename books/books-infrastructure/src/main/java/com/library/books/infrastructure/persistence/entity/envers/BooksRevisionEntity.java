package com.library.books.infrastructure.persistence.entity.envers;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.library.iam.infrastructure.security.CurrentUser;

@Entity
@Table(name = "REVINFO")
@RevisionEntity(BooksRevisionListener.class)
public class BooksRevisionEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @RevisionNumber
    @Column(name = "REV", nullable = false)
    private long rev;

    @RevisionTimestamp
    @Column(name = "REVTSTMP", nullable = false)
    private long timestamp;

    @Column(name = "username", length = 50)
    private String username;

    public long getRev() {
        return rev;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
