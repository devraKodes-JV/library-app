package com.library.iam.infrastructure.persistence.entity;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

/**
 * JPA entity for a role (persistence adapter).
 *
 * <p>The pure domain model is {@code com.library.iam.domain.model.Role}. The
 * {@code RoleMapper} converts between the two. The many-to-many relationship
 * to permissions is stored in the {@code role_permissions} join table.</p>
 *
 * <p>Extends {@link AuditableEntity} to gain audit columns
 * ({@code created_at}/{@code updated_at}/{@code created_by}/{@code updated_by})
 * and logical delete ({@code deleted_at}).</p>
 */
@Entity
@Table(name = "roles")
@Audited
public class RoleEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private boolean enabled = true;

    @ManyToMany
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissions = new LinkedHashSet<>();

    protected RoleEntity() {
        // Required by JPA.
    }

    public RoleEntity(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = true;
    }

    public RoleEntity(Long id, String name, String description, boolean enabled) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<PermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionEntity> permissions) {
        this.permissions = permissions;
    }
}
