package com.library.iam.infrastructure.web.controller.login;

import com.library.iam.domain.model.User;
import com.library.security.service.SecurityAuditService;

import io.javalin.http.Context;

public class LogoutController {

    private final SecurityAuditService auditService;

    public LogoutController(SecurityAuditService auditService) {
        this.auditService = auditService;
    }

    public void doLogout(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if (user != null) {
            auditService.audit(
                    "LOGOUT",
                    user.getUsername(),
                    ctx.ip(),
                    "User logged out"
            );
        }
        ctx.req().getSession().invalidate();
        ctx.redirect("/login?logout");
    }
}
