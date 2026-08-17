package com.library.bootstrap.upload;

import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Example upload endpoint using ImageStorage.
 *
 * <p>Move this to your domain controller when ready.</p>
 */
public final class UploadController {

    private static final Logger log = LoggerFactory.getLogger(UploadController.class);

    private UploadController() {
    }

    public static void uploadImage(Context ctx) {
        UploadedFile file = ctx.uploadedFile("image");
        if (file == null || file.content() == null) {
            ctx.status(400).result("Missing file");
            return;
        }

        try (InputStream is = file.content()) {
            byte[] bytes = is.readAllBytes();
            String filename = ImageStorage.store(bytes, file.filename());

            String scheme = "http";
            if (ctx.req().isSecure() || "https".equalsIgnoreCase(ctx.header("X-Forwarded-Proto"))) {
                scheme = "https";
            }
            String host = ctx.host();
            String url = scheme + "://" + host + "/uploads/" + filename;

            ctx.json(new java.util.HashMap<>() {{
                put("filename", filename);
                put("url", url);
                put("size", bytes.length);
            }});
        } catch (IOException e) {
            log.error("Upload failed", e);
            ctx.status(500).result("Upload failed");
        }
    }

    public static void serveImage(Context ctx) {
        String filename = ctx.pathParam("filename");
        try {
            Path path = ImageStorage.resolve(filename);
            ctx.contentType("application/octet-stream");
            ctx.result(Files.newInputStream(path));
        } catch (IOException e) {
            ctx.status(404).result("Not found");
        }
    }
}
