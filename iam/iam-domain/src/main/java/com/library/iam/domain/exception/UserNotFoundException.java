package com.library.iam.domain.exception;

/**
 * Domain exception thrown when a user cannot be found.
 *
 * <p>It lives in the domain layer and is used across the application
 * and infrastructure layers.</p>
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * @param username the username that was not found, included in the message
     *                 for easier debugging
     */
    public UserNotFoundException(String username) {
        super("User not found: " + username);
    }

    /**
     * @param id the user id that was not found, included in the message
     */
    public UserNotFoundException(Long id) {
        super("User not found: " + id);
    }
}
