package com.library.iam.infrastructure.persistence.repository.jpa;

import java.util.List;

import com.library.kernel.jpa.CrudRepository;
import com.library.kernel.jpa.FindableByUsername;

/**
 * Repository interface for {@link UserEntity}, composing the kernel
 * {@link UserRepository} capability contract and fixing the entity types.
 *
 * <p>The generic CRUD + find-by-username + uniqueness + password-update
 * operations are inherited from {@link UserRepository}. This concrete interface
 * only fixes the generic parameters to {@code UserEntity} / {@code Long}; the
 * Hibernate-backed implementation lives in
 * {@code repository.hibernate.HibernateUserRepository}.</p>
 */
public interface UserJpaRepository<T, ID> extends CrudRepository<T, ID>, FindableByUsername<T> {
    /**
     * Checks whether a username is already taken (excluding deleted users).
     *
     * @param username the login name to check
     * @return true if a user with that username exists
     */
    boolean existsByUsername(String username);

    /**
     * Checks whether an e-mail is already registered (excluding deleted users).
     *
     * @param email the e-mail to check
     * @return true if a user with that e-mail exists
     */
    boolean existsByEmail(String email);

/**
     * Updates only the password hash of a user, identified by username.
     *
     * <p>Used at startup to seed the default admin password. Keeping it as a
     * targeted operation avoids loading the whole entity graph.</p>
     *
     * @param username     the login name of the user to update
     * @param passwordHash the new encoded password hash
     * @return true if a user was updated
     */
    boolean updatePassword(String username, String passwordHash);

    /**
     * Lists users whose account is NOT usable (disabled or logically deleted).
     *
     * <p>Used by the users administration view to let an administrator review
     * and reinstate accounts. See {@code UserPort.findInactive()} for the exact
     * meaning of "not usable".</p>
     *
     * @return the list of inactive user entities (never null); may be empty
     */
    List<T> findInactive();

    /**
     * Reinstate a user account (double-check).
     *
     * <p>Clears BOTH the {@code enabled} flag (sets it to true) and the
     * {@code deleted_at} timestamp so the user can log in again. This is the
     * single recovery path for disabled OR deleted accounts.</p>
     *
     * @param id the id of the account to reinstate
     */
    void reinstate(ID id);
}
