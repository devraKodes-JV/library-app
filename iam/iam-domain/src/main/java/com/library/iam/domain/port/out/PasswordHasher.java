package com.library.iam.domain.port.out;

/**
 * Output (driven) port for password hashing.
 *
 * <p>Declares the two operations the application needs to manage passwords
 * without coupling it to any specific cryptography library. The actual
 * implementation lives in the infrastructure layer and uses a strong,
 * memory-hard and side-channel resistant algorithm (Argon2id via
 * BouncyCastle).</p>
 *
 * <p>Security note: Argon2id is the current recommended password hashing
 * algorithm (memory-hard, resistant to GPU cracking). It is also the basis
 * used by newer storage schemes. The {@code encodedHash} format is a
 * self-describing string (e.g. {@code $argon2id$v=19$...}) so parameters can
 * evolve without breaking verification.</p>
 */
public interface PasswordHasher {

    /**
     * Hashes a plain-text password.
     *
     * @param rawPassword the plain-text password (never store or log it)
     * @return the encoded, self-describing hash
     */
    String hash(String rawPassword);

    /**
     * Verifies a plain-text password against a previously stored hash.
     *
     * @param rawPassword the plain-text password typed by the user
     * @param encodedHash the stored encoded hash
     * @return true if the password matches the hash
     */
    boolean verify(String rawPassword, String encodedHash);
}
