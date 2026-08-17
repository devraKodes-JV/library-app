package com.library.iam.infrastructure.web.controller.module;

import java.util.List;
import java.util.Map;

import com.library.iam.application.dto.ModuleDTO;
import com.library.iam.application.service.module.ListModulesUseCase;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class ListModulesController {

    private final ListModulesUseCase listModulesUseCase;
    private final WebControllerContext webContext;

    public ListModulesController(ListModulesUseCase listModulesUseCase,
                                 WebControllerContext webContext) {
        this.listModulesUseCase = listModulesUseCase;
        this.webContext = webContext;
    }

    public void listModules(Context ctx) {
        requireCan(ctx, "modules.read");
        var current = webContext.currentUser(ctx);
        List<ModuleDTO> modules = listModulesUseCase.execute();
        List<?> sections = webContext.navSections(ctx);

        ctx.render("modules/list", Map.of(
                "user", current,
                "navSections", sections,
                "modules", modules));
    }

    private void requireCan(Context ctx, String permCode) {
        if (!webContext.hasPermission(ctx, permCode)) {
            throw new io.javalin.http.ForbiddenResponse();
        }
    }
}
