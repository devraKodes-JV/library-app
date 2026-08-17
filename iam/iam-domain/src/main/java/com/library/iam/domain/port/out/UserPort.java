package com.library.iam.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.library.iam.domain.model.User;

/**
 * Output (driven) port for user persistence.
 *
 * <p>In hexagonal architecture, an output port is the contract that the
 * domain needs to persist/retrieve data. It is implemented by an adapter in
 * the infrastructure layer (e.g. {@code UserPersistenceAdapter} using JPA).
 * The domain and application layers depend on this interface, never on a
 * concrete technology.</p>
 */
public interface UserPort {

    /**
     * Finds a user by username, if present.
     *
     * @param username the login name
     * @return an {@link Optional} containing the domain {@link User}, or empty
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a username is already taken.
     *
     * @param username the login name to check
     * @return true if a user with that username exists
     */
    boolean existsByUsername(String username);

    /**
     * Checks whether an e-mail is already registered.
     *
     * @param email the e-mail to check
     * @return true if a user with that e-mail exists
     */
    boolean existsByEmail(String email);

/**
     * Lists all non-deleted users.
     *
     * <p>Used by the users administration view. Only active (not logically
     * deleted) users are returned.</p>
     *
     * @return the list of active users (never null); may be empty
     */
    List<User> findAll();

    /**
     * Finds a non-deleted user by its identifier.
     *
     * @param id the user id
     * @return an {@link Optional} containing the domain {@link User}, or empty
     *         if it does not exist or was logically deleted
     */
    Optional<User> findById(Long id);

    /**
     * Lists users whose account is NOT usable, so an administrator can review
     * and reinstate them.
     *
     * <p>An account is considered "not usable" when EITHER of these holds:
     * <ul>
     *   <li><b>disabled</b>: {@code enabled = false} (the user cannot log in but
     *       the row is still actively visible in the system);</li>
     *   <li><b>deleted</b>: {@code deleted_at} is set (the row is hidden from
     *       the active list as if it were removed).</li>
     * </ul>
     * The users view shows both together under an "Inactive accounts" section
     * so an administrator can reactivate or restore them (double-check).</p>
     *
     * @return the list of inactive/not-usable users (never null); may be empty
     */
    List<User> findInactive();

    /**
     * Reinstate a user account (double-check).
     *
     * <p>Clears BOTH account flags so the user can log in again:
     * <ul>
     *   <li>sets {@code enabled = true};</li>
     *   <li>clears {@code deleted_at} (if the account was logically deleted).</li>
     * </ul>
     * This is the single recovery path that covers an account that was disabled
     * OR deleted by mistake. It requires the {@code users.reinstate} permission
     * (enforced by the web layer).</p>
     *
     * @param id the id of the account to reinstate
     */
    void reinstate(Long id);

    /**
     * Logically deletes a user by marking its {@code deleted_at} timestamp.
     *
     * <p>Per the product owner's decision, users are NOT physically removed:
     * they are soft-deleted so audit history and referential integrity are
     * preserved. A logically deleted user can no longer log in.</p>
     *
     * @param id the id of the user to delete
     */
    void delete(Long id);

/**

     * Persists a user (insert or update depending on whether it has an id).
     *
     * @param user the domain user to save
     * @return the saved user (with a populated id if it was newly inserted)
     */
    User save(User user);

    /**
     * Updates only the password hash of a user, identified by username.
     *
     * <p>Used at startup to seed the default admin password. Keeping it as a
     * targeted operation avoids loading the whole entity graph.</p>
     *
     * @param username the login name of the user to update
     * @param passwordHash the new encoded password hash
     * @return true if a user was updated
     */
    boolean updatePassword(String username, String passwordHash);
}
