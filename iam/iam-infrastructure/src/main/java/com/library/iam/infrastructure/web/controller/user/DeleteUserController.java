package com.library.iam.infrastructure.web.controller.user;

import com.library.iam.application.dto.command.user.DeleteUserCommand;
import com.library.iam.application.service.user.DeleteUserUseCase;
import com.library.kernel.web.WebControllerContext;
import com.library.kernel.web.WebHelper;

import io.javalin.http.Context;

public class DeleteUserController {

    private final DeleteUserUseCase deleteUserUseCase;
    private final WebControllerContext webContext;

    public DeleteUserController(DeleteUserUseCase deleteUserUseCase,
                                WebControllerContext webContext) {
        this.deleteUserUseCase = deleteUserUseCase;
        this.webContext = webContext;
    }

    public void deleteUser(Context ctx) {
        requireCan(ctx, "users.delete");
        long id = ctx.pathParamAsClass("id", Long.class).get();
        deleteUserUseCase.execute(new DeleteUserCommand(id));
        WebHelper.flashWarning(ctx, "User deleted. You can reinstate them from the inactive list.");
        ctx.redirect("/iam/users");
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
