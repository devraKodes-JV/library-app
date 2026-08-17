package com.library.iam.infrastructure.security;

import com.library.iam.domain.model.Role;
import com.library.iam.domain.model.User;
import com.library.security.service.SecurityAuditService;

import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.config.JavalinConfig;

public class SessionAuthFilter {

    public static final String USER_ATTR = "user";

    private SessionAuthFilter() {
    }

    public static void register(JavalinConfig config, SecurityAuditService auditService) {
        config.routes.before(ctx -> {
            if (isPublicPath(ctx.path())) {
                return;
            }
            User user = ctx.sessionAttribute(USER_ATTR);
            if (user == null) {
                auditService.audit(
                    "UNAUTHORIZED_ACCESS",
                    null,
                    ctx.ip(),
                    "Unauthenticated access to protected path: " + ctx.path()
                );
                ctx.redirect("/login");
                return;
            }
        });

        config.router.handlerWrapper(endpoint -> {
            return ctx -> {
                User user = ctx.sessionAttribute(USER_ATTR);
                if (user != null) {
                    ScopedValue.where(CurrentUser.USERNAME, user.getUsername()).run(() -> {
                        try {
                            endpoint.handler.handle(ctx);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    endpoint.handler.handle(ctx);
                }
            };
        });
    }

    private static boolean isPublicPath(String path) {
        return path.equals("/login")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/vendor/")
                || path.equals("/favicon.ico");
    }

    public static User requireUser(Context ctx) {
        User user = ctx.sessionAttribute(USER_ATTR);
        if (user == null) {
            throw new UnauthorizedResponse();
        }
        return user;
    }

    public static boolean hasRole(Context ctx, String roleName) {
        User user = requireUser(ctx);
        Role role = user.getRole();
        return role != null && roleName.equalsIgnoreCase(role.getName());
    }

    public static boolean hasPermission(Context ctx, String permCode) {
        User user = requireUser(ctx);
        Role role = user.getRole();
        return role != null && role.hasPermission(permCode);
    }
}
