package com.library.iam.infrastructure.web.controller.user;

import com.library.iam.application.dto.command.user.ReinstateUserCommand;
import com.library.iam.application.service.user.ReinstateUserUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class ReinstateUserController {

    private final ReinstateUserUseCase reinstateUserUseCase;
    private final WebControllerContext webContext;

    public ReinstateUserController(ReinstateUserUseCase reinstateUserUseCase,
                                   WebControllerContext webContext) {
        this.reinstateUserUseCase = reinstateUserUseCase;
        this.webContext = webContext;
    }

    public void reinstateUser(Context ctx) {
        requireCan(ctx, "users.reinstate");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        reinstateUserUseCase.execute(new ReinstateUserCommand(id));
        WebHelper.flashSuccess(ctx, "User reinstated successfully.");
        ctx.redirect("/iam/users");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
