package com.library.accounting.domain.model;

import java.time.Instant;

import com.library.accounting.domain.model.shared.AccountType;

public class Account {
    private Long id;
    private String code;
    private String name;
    private AccountType type;
    private String description;
    private boolean enabled = true;
    private Instant createdAt;
    private Instant updatedAt;

    public Account(Long id, String code, String name, AccountType type, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public static Account of(String code, String name, AccountType type, String description) {
        return new Account(null, code, name, type, description);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
