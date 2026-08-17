package com.library.iam.infrastructure.web.controller.user;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.kernel.web.WebHelper;

import com.library.iam.application.dto.UserDTO;
import com.library.iam.application.service.user.ListActiveUsersUseCase;
import com.library.iam.application.service.user.ListInactiveUsersUseCase;
import com.library.iam.application.service.role.ListRolesUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListUsersController {

    private static final Logger log = LoggerFactory.getLogger(ListUsersController.class);

    private final ListActiveUsersUseCase listActiveUsersUseCase;
    private final ListInactiveUsersUseCase listInactiveUsersUseCase;
    private final ListRolesUseCase listRolesUseCase;
    private final WebControllerContext webContext;

    public ListUsersController(ListActiveUsersUseCase listActiveUsersUseCase,
                               ListInactiveUsersUseCase listInactiveUsersUseCase,
                               ListRolesUseCase listRolesUseCase,
                               WebControllerContext webContext) {
        this.listActiveUsersUseCase = listActiveUsersUseCase;
        this.listInactiveUsersUseCase = listInactiveUsersUseCase;
        this.listRolesUseCase = listRolesUseCase;
        this.webContext = webContext;
    }

    public void listUsers(Context ctx) {
        requireCan(ctx, "users.read");
        var current = webContext.currentUser(ctx);
        List<UserDTO> active = listActiveUsersUseCase.execute();
        List<UserDTO> inactive = listInactiveUsersUseCase.execute();
        List<?> roles = listRolesUseCase.execute();
        List<?> sections = webContext.navSections(ctx);

        ctx.render("users/list", Map.of(
                "user", current,
                "navSections", sections,
                "activeUsers", active,
                "inactiveUsers", inactive,
                "roles", roles,
                "canCreate", webContext.hasPermission(ctx, "users.create"),
                "canUpdate", webContext.hasPermission(ctx, "users.update"),
                "canDelete", webContext.hasPermission(ctx, "users.delete"),
                "canReinstate", webContext.hasPermission(ctx, "users.reinstate")));
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
