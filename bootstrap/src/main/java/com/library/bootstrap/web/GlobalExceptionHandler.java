package com.library.bootstrap.web;

import java.util.List;
import java.util.Map;

import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;
import io.javalin.http.ExceptionHandler;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UnauthorizedResponse;

public class GlobalExceptionHandler implements ExceptionHandler<Exception> {

    @Override
    public void handle(Exception error, Context ctx) {
        try {
            if (error instanceof NotFoundResponse) {
                renderError(ctx, 404, "errors/404", "Page Not Found", "The page you are looking for does not exist or has been moved.");
                return;
            }

            if (error instanceof UnauthorizedResponse) {
                renderError(ctx, 401, "errors/401", "Unauthorized", "You need to log in to access this page.");
                return;
            }

            if (error instanceof ForbiddenResponse) {
                renderError(ctx, 403, "errors/403", "Forbidden", "You do not have permission to access this page.");
                return;
            }

            String message = error.getMessage();
            if (message == null || message.isBlank()) {
                message = error.getClass().getName();
            }
            renderError(ctx, 500, "errors/500", "Server Error", "Something went wrong: " + message);
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("Server Error");
        }
    }

    private void renderError(Context ctx, int status, String template, String title, String message) {
        ctx.status(status);
        try {
            ctx.render(template, Map.of(
                    "user", currentUser(ctx),
                    "navSections", navSections(ctx),
                    "errorTitle", title,
                    "errorMessage", message
            ));
        } catch (Exception e) {
            String user = currentUser(ctx) != null ? currentUser(ctx).toString() : "Guest";
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

    private Object currentUser(Context ctx) {
        WebControllerContext webContext = ctx.attribute("webContext");
        if (webContext != null) {
            return webContext.currentUser(ctx);
        }
        return null;
    }

    private List<?> navSections(Context ctx) {
        WebControllerContext webContext = ctx.attribute("webContext");
        if (webContext != null) {
            return webContext.navSections(ctx);
        }
        return List.of();
    }
}
