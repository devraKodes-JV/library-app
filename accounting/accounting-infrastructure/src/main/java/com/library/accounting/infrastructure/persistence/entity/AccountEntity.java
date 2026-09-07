package com.library.accounting.infrastructure.persistence.entity;

import java.io.Serializable;

import org.hibernate.envers.Audited;

import com.library.accounting.domain.model.Account;
import com.library.accounting.domain.model.shared.AccountType;
import com.library.iam.infrastructure.persistence.entity.AuditableEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "accounts")
@Audited
public class AccountEntity extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 200)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType type;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean enabled = true;

    public AccountEntity() {}

    public AccountEntity(Long id, String code, String name, AccountType type,
                         String description, boolean enabled) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.description = description;
        this.enabled = enabled;
    }

    public static AccountEntity fromDomain(Account account) {
        return new AccountEntity(account.getId(), account.getCode(), account.getName(),
                account.getType(), account.getDescription(), account.isEnabled());
    }

    public Account toDomain() {
        Account a = new Account(id, code, name, type, description);
        a.setEnabled(enabled);
        a.setCreatedAt(getCreatedAt());
        a.setUpdatedAt(getUpdatedAt());
        return a;
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
}
