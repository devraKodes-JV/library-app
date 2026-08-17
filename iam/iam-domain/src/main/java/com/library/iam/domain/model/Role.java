package com.library.iam.domain.model;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Role of the system (pure domain object, no framework dependency).
 *
 * <p>A role groups a set of permissions. Examples: ADMIN (all permissions),
 * EMPLOYEE (manages the catalogue), CLIENT (read-only and reservations).</p>
 *
 * <p>Part of the pure domain layer ({@code iam-domain} module). It has real
 * business behaviour such as {@link #hasPermission(String)} which is used by
 * the security layer to decide access at runtime.</p>
 */
public class Role {

private Long id;
    private String name;
    private String description;
    private boolean enabled = true;
    private Set<Permission> permissions = new LinkedHashSet<>();

    /**
     * @param id          database identifier (null for a not-yet-persisted role)
     * @param name        unique role name (ADMIN, EMPLOYEE, CLIENT)
     * @param description human readable description of the role
     */
    public Role(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /** Convenience factory for a new (not yet persisted, enabled) role. */
    public static Role withoutId(String name, String description) {
        Role role = new Role(null, name, description);
        role.enabled = true;
        return role;
    }

    /**
     * Adds a permission to this role. The {@link LinkedHashSet} keeps insertion
     * order so the menu ordering stays stable.
     *
     * @param permission the permission to grant
     */
    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    /** Removes a permission from this role. */
    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }

    /**
     * Checks whether this role has a permission identified by its code.
     *
     * @param code permission code (e.g. "books.read", "users.manage")
     * @return true if any of the role's permissions has that code
     */
    public boolean hasPermission(String code) {
        return permissions.stream().anyMatch(p -> p.getCode().equals(code));
    }

    // Getters and setters.

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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
