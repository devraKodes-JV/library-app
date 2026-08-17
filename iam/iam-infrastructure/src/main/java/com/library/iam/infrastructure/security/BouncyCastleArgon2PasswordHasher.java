package com.library.iam.infrastructure.security;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import com.library.iam.domain.port.out.PasswordHasher;

/**
 * Argon2id password hasher implemented with BouncyCastle.
 *
 * <p>Argon2id is the current recommended password hashing algorithm (memory
 * hard, CPU hard, resistant to GPU/ASIC cracking). The encoded hash is a
 * self-describing string so parameters can be tuned or upgraded without
 * breaking existing hashes:</p>
 *
 * <pre>
 * $argon2id$v=19$m=19456,t=2,p=1$<salt-base64>$<hash-base64>
 * </pre>
 *
 * <p>Field sizes are tuned for a desktop/biblioteca server (192 MiB memory,
 * 2 iterations, 1 lane). The salt is 16 random bytes per hash.</p>
 */
public class BouncyCastleArgon2PasswordHasher implements PasswordHasher {

    /** Memory cost in KiB. 19456 KiB ~= 19 MiB. */
    private static final int MEMORY_KIB = 19456;
    /** Iterations (time cost). */
    private static final int ITERATIONS = 2;
    /** Parallelism (lanes). */
    private static final int PARALLELISM = 1;
    /** Salt length in bytes (128 bits). */
    private static final int SALT_LENGTH = 16;
    /** Output hash length in bytes (256 bits). */
    private static final int HASH_LENGTH = 32;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String hash(String rawPassword) {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        byte[] hash = runArgon2(rawPassword, salt);
        return encode(salt, hash);
    }

    @Override
    public boolean verify(String rawPassword, String encodedHash) {
        try {
            Decoded decoded = decode(encodedHash);
            byte[] candidate = runArgon2(rawPassword, decoded.salt());
            return constantTimeEquals(decoded.hash(), candidate);
        } catch (IllegalArgumentException e) {
            // Malformed hash: never treat as a valid password.
            return false;
        }
    }

    /**
     * Runs the Argon2id derivation using the configured parameters.
     *
     * @param rawPassword the plain-text password bytes
     * @param salt        the random salt bytes
     * @return the derived hash bytes
     */
    private byte[] runArgon2(String rawPassword, byte[] salt) {
        Argon2Parameters params = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withMemoryAsKB(MEMORY_KIB)
                .withIterations(ITERATIONS)
                .withParallelism(PARALLELISM)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(params);

        byte[] hash = new byte[HASH_LENGTH];
        generator.generateBytes(rawPassword.getBytes(StandardCharsets.UTF_8), hash);
        return hash;
    }

    /**
     * Serializes the hash into the self-describing {@code $argon2id$...} format.
     */
    private String encode(byte[] salt, byte[] hash) {
        return "$argon2id$v=19$m=" + MEMORY_KIB
                + ",t=" + ITERATIONS
                + ",p=" + PARALLELISM
                + "$" + Base64.getEncoder().encodeToString(salt)
                + "$" + Base64.getEncoder().encodeToString(hash);
    }

/**
     * Parses the self-describing hash back into its parts.
     *
     * <p>The encoded string is {@code $argon2id$v=19$m=...,t=...,p=...$salt$hash}.
     * Because it starts and separates fields with {@code $}, splitting on
     * {@code $} yields SIX parts: {@code ["", "argon2id", "v=19",
     * "m=...,t=...,p=...", "<salt>", "<hash>"]}. Part #1 always exists but is
     * empty (the content before the leading {@code $}).</p>
     */
    private Decoded decode(String encoded) {
        String[] parts = encoded.split("\\$");
        // parts.length == 6: ["", "argon2id", "v=19", "params", salt, hash]
        if (parts.length != 6 || !"argon2id".equals(parts[1])) {
            throw new IllegalArgumentException("Unsupported or malformed hash");
        }
        byte[] salt = Base64.getDecoder().decode(parts[4]);
        byte[] hash = Base64.getDecoder().decode(parts[5]);
        return new Decoded(salt, hash);
    }

    /**
     * Constant-time comparison to avoid timing side channels.
     */
    private boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    private record Decoded(byte[] salt, byte[] hash) {
    }
}
