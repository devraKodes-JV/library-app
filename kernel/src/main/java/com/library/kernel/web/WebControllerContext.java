package com.library.kernel.web;

import java.util.List;

public interface WebControllerContext {
    Object currentUser(Object requestContext);
    List<?> navSections(Object requestContext);
    boolean hasPermission(Object requestContext, String permCode);
}
