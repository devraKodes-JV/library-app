package com.library.iam.infrastructure.web.controller.dashboard;

import java.util.List;
import java.util.Map;

import com.library.iam.application.dto.NavSection;
import com.library.iam.application.service.navigation.BuildNavigationUseCase;
import com.library.iam.domain.model.User;

import io.javalin.http.Context;

public class ShowDashboardController {

    private final BuildNavigationUseCase buildNavigationUseCase;

    public ShowDashboardController(BuildNavigationUseCase buildNavigationUseCase) {
        this.buildNavigationUseCase = buildNavigationUseCase;
    }

    public void showDashboard(Context ctx) {
        User user = ctx.sessionAttribute("user");
        List<NavSection> sections = buildNavigationUseCase.execute(user);

        ctx.render("dashboard", Map.of(
                "user", user,
                "navSections", sections));
    }
}
