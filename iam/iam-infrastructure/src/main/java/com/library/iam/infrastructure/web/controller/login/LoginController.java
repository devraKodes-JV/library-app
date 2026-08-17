package com.library.iam.infrastructure.web.controller.login;

import java.util.Map;

import com.library.iam.application.service.login.LoadUserByUsernameUseCase;
import com.library.iam.application.service.password.PasswordOperationsUseCase;
import com.library.iam.domain.model.User;
import com.library.iam.domain.service.AccountLockoutService;
import com.library.security.service.SecurityAuditService;

import io.javalin.http.Context;

public class LoginController {

    private final LoadUserByUsernameUseCase loadUserByUsernameUseCase;
    private final PasswordOperationsUseCase passwordOperationsUseCase;
    private final SecurityAuditService auditService;

    public LoginController(LoadUserByUsernameUseCase loadUserByUsernameUseCase,
                           PasswordOperationsUseCase passwordOperationsUseCase,
                           SecurityAuditService auditService) {
        this.loadUserByUsernameUseCase = loadUserByUsernameUseCase;
        this.passwordOperationsUseCase = passwordOperationsUseCase;
        this.auditService = auditService;
    }

    public void showLogin(Context ctx) {
        String error = ctx.queryParam("error");
        String logout = ctx.queryParam("logout");
        Map<String, Object> model = new java.util.HashMap<>();
        model.put("loginError", error != null);
        model.put("logoutMessage", logout != null);
        if (error != null) {
            model.put("errorMessage", "Invalid username or password.");
        } else if (logout != null) {
            model.put("successMessage", "You have been signed out.");
        }
        ctx.render("login", model);
    }

    public void doLogin(Context ctx) {
        String username = ctx.formParam("username");
        String rawPassword = ctx.formParam("password");
        String clientIp = ctx.ip();

        User user = findUser(username);

        if (user != null && user.isEnabled()) {
            if (!AccountLockoutService.isLocked(user.getFailedLoginAttempts(), user.getLockedUntil())) {
                if (passwordOperationsUseCase.verify(rawPassword, user.getPassword())) {
                    completeLogin(ctx, user, clientIp);
                    return;
                }
                recordFailedLogin(user, username, clientIp);
            } else {
                auditService.audit(
                        "LOGIN_LOCKED",
                        username,
                        clientIp,
                        "Account locked due to failed attempts"
                );
            }
        } else if (user == null) {
            passwordOperationsUseCase.verify(rawPassword, DUMMY_HASH);
        }

        ctx.redirect("/login?error=1");
    }

    private static final String DUMMY_HASH;

    static {
        byte[] salt = new byte[16];
        byte[] hash = new byte[32];
        new java.security.SecureRandom().nextBytes(salt);
        new java.security.SecureRandom().nextBytes(hash);
        DUMMY_HASH = "$argon2id$v=19$m=19456,t=2,p=1$"
                + java.util.Base64.getEncoder().encodeToString(salt) + "$"
                + java.util.Base64.getEncoder().encodeToString(hash);
    }

    private User findUser(String username) {
        try {
            return loadUserByUsernameUseCase.execute(username);
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(LoginController.class)
                    .debug("User not found: {}", username);
            return null;
        }
    }

    private void recordFailedLogin(User user, String username, String clientIp) {
        AccountLockoutService.FailedResult result = AccountLockoutService.recordFailedAttempt(
                user.getFailedLoginAttempts(), user.getLockedUntil());
        user.setFailedLoginAttempts(result.failedAttempts());
        user.setLockedUntil(result.lockedUntil());
        loadUserByUsernameUseCase.save(user);

        int remaining = AccountLockoutService.getMaxFailedAttempts() - user.getFailedLoginAttempts();
        auditService.audit(
                "LOGIN_FAILED",
                username,
                clientIp,
                "Invalid credentials. " + remaining + " attempts remaining before lockout."
        );
    }

    private void completeLogin(Context ctx, User user, String clientIp) {
        try {
            AccountLockoutService.ResetResult reset = AccountLockoutService.resetFailedAttempts();
            user.setFailedLoginAttempts(reset.failedAttempts());
            user.setLockedUntil(reset.lockedUntil());
            loadUserByUsernameUseCase.save(user);

            ctx.sessionAttribute("user", user);
            ctx.req().changeSessionId();
            org.slf4j.LoggerFactory.getLogger(LoginController.class)
                    .info("User logged in: {}", user.getUsername());
            auditService.audit(
                    "LOGIN_SUCCESS",
                    user.getUsername(),
                    clientIp,
                    "Successful authentication"
            );
            ctx.redirect("/");
        } catch (Exception e) {
            org.slf4j.LoggerFactory.getLogger(LoginController.class)
                    .error("Error completing login for '{}'", user.getUsername(), e);
            ctx.redirect("/login?error=1");
        }
    }
}
