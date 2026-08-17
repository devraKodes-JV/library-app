package com.library.bootstrap.config;

import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jetty.http.HttpCookie;
import com.library.bootstrap.factory.AppFactory;
import com.library.bootstrap.upload.UploadController;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.Javalin;

public class JavalinStart {
    
    private static final int MAX_ERROR_MESSAGE_LENGTH = 200;
    private static final java.util.regex.Pattern SENSITIVE_PATTERN = java.util.regex.Pattern.compile(
        "(?i)(password|passwd|pwd|secret|api[_-]?key|apikey|token|bearer|private[_-]?key|credential|auth|session|cookie|jwt|oauth|" +
        "select\\s.*\\bfrom\\b|insert\\s.*\\binto\\b|update\\s.*\\bset\\b|delete\\s.*\\bfrom\\b|" +
        "/etc/passwd|/etc/shadow|/proc/|/root/|/windows/|/system32/|connection[_-]?string|jdbc|mongodb|redis|" +
        "[a-f0-9]{32,}|[A-Za-z0-9+/]{40,}={0,2})"
    );
    
    public static void run(SessionFactory sessionFactory, Logger log){
        Javalin.create(config -> {
            config.staticFiles.add("/static");
            config.fileRenderer(new PebbleFileRenderer());
            
            config.jetty.modifyServletContextHandler(handler -> {
                handler.getSessionHandler().setHttpOnly(true);
                handler.getSessionHandler().setSameSite(HttpCookie.SameSite.STRICT);
                jakarta.servlet.SessionCookieConfig cookieConfig = handler.getSessionHandler().getSessionCookieConfig();
                cookieConfig.setSecure(Boolean.parseBoolean(System.getProperty("https", "false")));
                cookieConfig.setMaxAge(3600);
            });
            
            config.jetty.modifyServer(server -> {
                server.setStopTimeout(5000);
                server.setStopAtShutdown(true);
            });
            
            config.jetty.modifyHttpConfiguration(http -> {
                http.setSendServerVersion(false);
                http.setSendDateHeader(false);
            });
            
            AppFactory.create(sessionFactory, config);

            config.routes.post("/upload", ctx -> UploadController.uploadImage(ctx));
            config.routes.get("/uploads/{filename}", ctx -> UploadController.serveImage(ctx));

            config.routes.exception(Exception.class, new com.library.bootstrap.web.GlobalExceptionHandler());

            config.routes.error(404, ctx -> renderError(ctx, 404, "errors/404", "Page Not Found", "The page you are looking for does not exist or has been moved."));
            config.routes.error(500, ctx -> renderError(ctx, 500, "errors/500", "Server Error", "Something went wrong on our end. Please try again later."));
        }).start(AppConfig.PORT);
    }
    
    private static void renderError(Context ctx, int status, String template, String title, String message) {
        ctx.status(status);
        try {
            ctx.render(template, Map.of(
                    "errorTitle", title,
                    "errorMessage", message
            ));
        } catch (Exception e) {
            String html = "<!DOCTYPE html><html><head><title>" + title + "</title>" +
                    "<link rel=\"stylesheet\" href=\"/css/bootstrap.min.css\"></head>" +
                    "<body class=\"bg-dark text-light\"><div class=\"container mt-5\">" +
                    "<div class=\"card\"><div class=\"card-body text-center\">" +
                    "<h1 class=\"display-1\">" + status + "</h1>" +
                    "<h2>" + title + "</h2>" +
                    "<p>" + message + "</p>" +
                    "<a href=\"/\" class=\"btn btn-primary\">Go Home</a>" +
                    "</div></div></div></body></html>";
            ctx.html(html);
        }
    }
    
    private static String sanitizeErrorMessage(String message) {
        if (message == null) {
            return "An error occurred";
        }
        if (SENSITIVE_PATTERN.matcher(message).find()) {
            return "An error occurred (sensitive information hidden)";
        }
        if (message.length() > MAX_ERROR_MESSAGE_LENGTH) {
            return message.substring(0, MAX_ERROR_MESSAGE_LENGTH) + "...";
        }
        return message;
    }
}
