package com.library.iam.infrastructure.web.controller.role;

import com.library.iam.application.dto.command.role.ReinstateRoleCommand;
import com.library.iam.application.service.role.ReinstateRoleUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class ReinstateRoleController {

    private final ReinstateRoleUseCase reinstateRoleUseCase;
    private final WebControllerContext webContext;

    public ReinstateRoleController(ReinstateRoleUseCase reinstateRoleUseCase,
                                   WebControllerContext webContext) {
        this.reinstateRoleUseCase = reinstateRoleUseCase;
        this.webContext = webContext;
    }

    public void reinstateRole(Context ctx) {
        requireCan(ctx, "roles.reinstate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        reinstateRoleUseCase.execute(new ReinstateRoleCommand(id));
        WebHelper.flashSuccess(ctx, "Role reinstated successfully.");
        ctx.redirect("/iam/roles");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
