package com.library.iam.infrastructure.web;

import java.util.List;
import java.util.function.Function;

import com.library.iam.application.dto.NavSection;
import com.library.iam.application.service.navigation.BuildNavigationUseCase;
import com.library.iam.domain.model.User;
import com.library.iam.infrastructure.security.SessionAuthFilter;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

public class IamWebControllerContext implements WebControllerContext {

    private final Function<User, List<NavSection>> navigationBuilder;

    public IamWebControllerContext(Function<User, List<NavSection>> navigationBuilder) {
        this.navigationBuilder = navigationBuilder;
    }

    @Override
    public Object currentUser(Object requestContext) {
        return SessionAuthFilter.requireUser((Context) requestContext);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<NavSection> navSections(Object requestContext) {
        User current = (User) currentUser(requestContext);
        return navigationBuilder.apply(current);
    }

    @Override
    public boolean hasPermission(Object requestContext, String permCode) {
        return SessionAuthFilter.hasPermission((Context) requestContext, permCode);
    }
}
