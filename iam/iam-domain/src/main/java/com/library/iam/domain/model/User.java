package com.library.iam.domain.model;

import java.time.Instant;

/**
 * User of the system (pure domain object, no framework dependency).
 *
 * <p>A user has exactly one role. The effective permissions of the user are
 * derived from the permissions associated with that role.</p>
 *
 * <p>THIS CLASS IS PART OF THE PURE DOMAIN LAYER. It lives in the
 * {@code iam-domain} module and has ZERO dependencies on infrastructure
 * (no JPA annotations, no HTTP framework, no templates). The JPA entity that
 * persists it ({@code UserEntity}) lives in the infrastructure layer and is
 * kept deliberately separate so the business rules stay clean and testable.</p>
 */
public class User {

    private Long id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private boolean enabled;
    private Role role;
    private int failedLoginAttempts;
    private Instant lockedUntil;

    /**
     * Timestamp of the logical delete (soft delete), or {@code null} if the
     * account is active.
     *
     * <p>Exposed on the domain model so the users administration view can label
     * an inactive account as "disabled" ({@code enabled = false}) versus
     * "deleted" ({@code deletedAt != null}). Both kinds of inactive accounts are
     * listed together in the reinstate screen and can be recovered with the
     * {@code users.reinstate} permission.</p>
     */
    private Instant deletedAt;

    /**
     * Full constructor. All fields are mutable so the domain object can be
     * hydrated by the persistence adapter and mutated by application services.
     *
     * @param id       database identifier (null for a not-yet-persisted user)
     * @param username login name (unique)
     * @param password encoded password hash (never stored in plain text)
     * @param fullName display name of the person
     * @param email    contact e-mail (unique)
     * @param enabled  whether the account is active (disabled user cannot log in)
     * @param role     the single role assigned to the user
     */
    public User(Long id, String username, String password, String fullName,
                String email, boolean enabled, Role role) {
        this(id, username, password, fullName, email, enabled, role, 0, null);
    }
    
    public User(Long id, String username, String password, String fullName,
                String email, boolean enabled, Role role, int failedLoginAttempts, Instant lockedUntil) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.enabled = enabled;
        this.role = role;
        this.failedLoginAttempts = failedLoginAttempts;
        this.lockedUntil = lockedUntil;
    }

    /**
     * Convenience factory for a new (not yet persisted) user.
     *
     * @return a {@link User} with null id
     */
    public static User withoutId(String username, String password, String fullName,
                                 String email, boolean enabled, Role role) {
        return new User(null, username, password, fullName, email, enabled, role, 0, null);
    }

    // ------------------------------------------------------------------
    // Getters and setters (explicit, no Lombok, for clarity and control).
    // ------------------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(int failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public Instant getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(Instant lockedUntil) {
        this.lockedUntil = lockedUntil;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * Whether the account was logically deleted (soft delete).
     *
     * <p>Distinct from being merely disabled: a deleted account has its
     * {@code deleted_at} set and is hidden from the active list, while a
     * disabled account has {@code enabled = false} but is still listed. Both
     * are inactive and appear in the reinstate screen.</p>
     *
     * @return true if the account was logically deleted
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }
}
