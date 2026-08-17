package com.library.iam.infrastructure.web.controller.role;

import com.library.iam.application.dto.command.role.DeleteRoleCommand;
import com.library.iam.application.service.role.DeleteRoleUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class DeleteRoleController {

    private final DeleteRoleUseCase deleteRoleUseCase;
    private final WebControllerContext webContext;

    public DeleteRoleController(DeleteRoleUseCase deleteRoleUseCase,
                                WebControllerContext webContext) {
        this.deleteRoleUseCase = deleteRoleUseCase;
        this.webContext = webContext;
    }

    public void deleteRole(Context ctx) {
        requireCan(ctx, "roles.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteRoleUseCase.execute(new DeleteRoleCommand(id));
        WebHelper.flashWarning(ctx, "Role deleted. You can reinstate it from the inactive list.");
        ctx.redirect("/iam/roles");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
