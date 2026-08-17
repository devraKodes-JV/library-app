package com.library.iam.infrastructure.web;

import com.library.iam.infrastructure.notification.SseNotificationService;
import com.library.iam.infrastructure.security.SessionAuthFilter;
import com.library.iam.infrastructure.web.controller.dashboard.ShowDashboardController;
import com.library.iam.infrastructure.web.controller.login.LoginController;
import com.library.iam.infrastructure.web.controller.login.LogoutController;
import com.library.iam.infrastructure.web.controller.module.ListModulesController;
import com.library.iam.infrastructure.web.controller.permission.ListPermissionsController;
import com.library.iam.infrastructure.web.controller.role.CreateRoleController;
import com.library.iam.infrastructure.web.controller.role.DeleteRoleController;
import com.library.iam.infrastructure.web.controller.role.ListRolesController;
import com.library.iam.infrastructure.web.controller.role.UpdateRoleController;
import com.library.iam.infrastructure.web.controller.user.CreateUserController;
import com.library.iam.infrastructure.web.controller.user.DeleteUserController;
import com.library.iam.infrastructure.web.controller.user.DisableUserController;
import com.library.iam.infrastructure.web.controller.user.ListUsersController;
import com.library.iam.infrastructure.web.controller.user.ReinstateUserController;
import com.library.iam.infrastructure.web.controller.user.UpdateUserController;
import com.library.security.service.SecurityAuditService;

import io.javalin.config.JavalinConfig;
import io.javalin.http.sse.SseHandler;

public final class IamRoutes {

    private static final String NOTIFICATIONS_STREAM = "notifications.stream";

    private IamRoutes() {
    }

    public static void register(JavalinConfig config,
                                LoginController loginController,
                                LogoutController logoutController,
                                ShowDashboardController showDashboardController,
                                ListRolesController listRolesController,
                                CreateRoleController createRoleController,
                                UpdateRoleController updateRoleController,
                                DeleteRoleController deleteRoleController,
                                ListUsersController listUsersController,
                                CreateUserController createUserController,
                                UpdateUserController updateUserController,
                                DeleteUserController deleteUserController,
                                DisableUserController disableUserController,
                                ReinstateUserController reinstateUserController,
                                ListPermissionsController listPermissionsController,
                                ListModulesController listModulesController,
                                SseNotificationService notificationService,
                                SecurityAuditService auditService) {
        SessionAuthFilter.register(config, auditService);

        config.routes.get("/login", loginController::showLogin);
        config.routes.post("/login", loginController::doLogin);
        config.routes.post("/logout", logoutController::doLogout);

        config.routes.get("/", showDashboardController::showDashboard);

        config.routes.get("/iam/roles", listRolesController::listRoles);
        config.routes.get("/iam/roles/new", createRoleController::showCreateForm);
        config.routes.post("/iam/roles", createRoleController::createRole);
        config.routes.get("/iam/roles/{id}/edit", updateRoleController::showEditForm);
        config.routes.post("/iam/roles/{id}", updateRoleController::updateRole);
        config.routes.post("/iam/roles/{id}/delete", deleteRoleController::deleteRole);

        config.routes.get("/iam/users", listUsersController::listUsers);
        config.routes.get("/iam/users/new", createUserController::showCreateForm);
        config.routes.post("/iam/users", createUserController::createUser);
        config.routes.get("/iam/users/{id}/edit", updateUserController::showEditForm);
        config.routes.post("/iam/users/{id}", updateUserController::updateUser);
        config.routes.post("/iam/users/{id}/disable", disableUserController::disableUser);
        config.routes.post("/iam/users/{id}/delete", deleteUserController::deleteUser);
        config.routes.post("/iam/users/{id}/reinstate", reinstateUserController::reinstateUser);

        config.routes.get("/iam/permissions", listPermissionsController::listPermissions);

        config.routes.get("/iam/modules", listModulesController::listModules);

        config.routes.get("/api/notifications/stream", new SseHandler(streamClient ->
                requirePermissionThenConnect(notificationService, streamClient)));
    }

    private static void requirePermissionThenConnect(SseNotificationService notificationService,
                                                     io.javalin.http.sse.SseClient client) {
        if (!SessionAuthFilter.hasPermission(client.ctx(), NOTIFICATIONS_STREAM)) {
            client.close();
            return;
        }
        notificationService.addClient(client);
    }
}
