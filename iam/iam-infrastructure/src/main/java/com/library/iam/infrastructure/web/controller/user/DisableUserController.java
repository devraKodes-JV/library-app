package com.library.iam.infrastructure.web.controller.user;

import com.library.iam.application.dto.command.user.DisableUserCommand;
import com.library.iam.application.service.user.DisableUserUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class DisableUserController {

    private final DisableUserUseCase disableUserUseCase;
    private final WebControllerContext webContext;

    public DisableUserController(DisableUserUseCase disableUserUseCase,
                                 WebControllerContext webContext) {
        this.disableUserUseCase = disableUserUseCase;
        this.webContext = webContext;
    }

    public void disableUser(Context ctx) {
        requireCan(ctx, "users.update");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        disableUserUseCase.execute(new DisableUserCommand(id));
        WebHelper.flashWarning(ctx, "User account disabled.");
        ctx.redirect("/iam/users");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
