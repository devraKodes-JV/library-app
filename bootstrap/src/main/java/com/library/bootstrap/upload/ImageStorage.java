package com.library.bootstrap.upload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Image storage service for local uploads.
 *
 * <p>Stores uploaded images under {@code data/uploads/} with:
 * <ul>
 *   <li>Magic-bytes validation (no trust in file extension)</li>
 *   <li>Size limit</li>
 *   <li>Unique filename (UUID) to prevent overwrite and path traversal</li>
 * </ul>
 */
public final class ImageStorage {

    private static final Path UPLOAD_DIR = Path.of("data", "uploads");
    private static final long MAX_SIZE_BYTES = 2 * 1024 * 1024; // 2MB

    private static final byte[] PNG = {(byte) 0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    private static final byte[] JPEG = {(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
    private static final byte[] GIF = {0x47, 0x49, 0x46, 0x38};
    private static final byte[] WEBP_RIFF = {0x52, 0x49, 0x46, 0x46};
    private static final byte[] WEBP_WEBP = {0x57, 0x45, 0x42, 0x50}; // offset 8

    private ImageStorage() {
    }

    public static void init() {
        try {
            Files.createDirectories(UPLOAD_DIR);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create upload directory: " + UPLOAD_DIR, e);
        }
    }

    public static String store(byte[] bytes, String originalFilename) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Empty file");
        }
        if (bytes.length > MAX_SIZE_BYTES) {
            throw new IllegalArgumentException("File too large: max " + MAX_SIZE_BYTES + " bytes");
        }

        String extension = detectExtension(bytes);
        String filename = UUID.randomUUID() + "." + extension;
        Path target = UPLOAD_DIR.resolve(filename);

        try {
            Files.write(target, bytes);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot store uploaded image", e);
        }

        return filename;
    }

    public static Path resolve(String filename) {
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Invalid filename");
        }
        Path resolved = UPLOAD_DIR.resolve(filename).normalize();
        if (!resolved.startsWith(UPLOAD_DIR.normalize())) {
            throw new IllegalArgumentException("Path traversal detected");
        }
        return resolved;
    }

    public static void delete(String filename) {
        try {
            Path path = resolve(filename);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            // Log in real app
        }
    }

    private static String detectExtension(byte[] bytes) {
        if (startsWith(bytes, PNG)) return "png";
        if (startsWith(bytes, JPEG)) return "jpg";
        if (startsWith(bytes, GIF)) return "gif";
        if (bytes.length >= 12 && startsWith(bytes, WEBP_RIFF) && startsWith(bytes, WEBP_WEBP, 8)) {
            return "webp";
        }
        throw new IllegalArgumentException("Unsupported image format");
    }

    private static boolean startsWith(byte[] source, byte[] prefix) {
        return startsWith(source, prefix, 0);
    }

    private static boolean startsWith(byte[] source, byte[] prefix, int offset) {
        if (source.length < offset + prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (source[offset + i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }
}
