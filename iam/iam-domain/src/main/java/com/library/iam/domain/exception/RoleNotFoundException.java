package com.library.iam.domain.exception;

/**
 * Domain exception thrown when a role cannot be found.
 *
 * <p>It is a pure domain object (no framework dependency). The application
 * layer throws it inside use cases when a requested role does not exist or has
 * been logically deleted, and the web layer maps it to an HTTP response.</p>
 */
public class RoleNotFoundException extends RuntimeException {

    /**
     * @param id the id of the role that was not found
     */
    public RoleNotFoundException(Long id) {
        super("Role not found: " + id);
    }

    /**
     * @param name the name of the role that was not found
     */
    public RoleNotFoundException(String name) {
        super("Role not found: " + name);
    }
}
