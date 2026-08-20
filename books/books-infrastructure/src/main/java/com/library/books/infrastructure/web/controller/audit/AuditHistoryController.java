package com.library.books.infrastructure.web.controller.audit;

import java.util.List;
import java.util.Map;

import com.library.books.infrastructure.audit.AuditQueryService;
import com.library.kernel.web.WebControllerContext;

import io.javalin.http.Context;

import com.library.kernel.web.BaseController;

public class AuditHistoryController extends BaseController {

    private final AuditQueryService auditQueryService;

    public AuditHistoryController(AuditQueryService auditQueryService, WebControllerContext webContext) {
        super(webContext);
        this.auditQueryService = auditQueryService;
    }

    public void showRevisions(Context ctx) {
        requireCan(ctx, "audit.read");
        String entityClass = ctx.queryParam("entityClass");
        Long entityId = parseLong(ctx.queryParam("entityId"));

        if (entityClass == null || entityId == null) {
            ctx.render("audit/revisions", Map.of(
                    "user", currentUser(ctx),
                    "navSections", navSections(ctx),
                    "revisions", List.of(),
                    "error", "Missing entityClass or entityId"));
            return;
        }

        try {
            Class<?> clazz = Class.forName(entityClass);
            List<?> revisions = auditQueryService.getRevisions(clazz, entityId);
            ctx.render("audit/revisions", Map.of(
                    "user", currentUser(ctx),
                    "navSections", navSections(ctx),
                    "revisions", revisions,
                    "entityClass", entityClass,
                    "entityId", entityId,
                    "error", null));
        } catch (ClassNotFoundException e) {
            ctx.render("audit/revisions", Map.of(
                    "user", currentUser(ctx),
                    "navSections", navSections(ctx),
                    "revisions", List.of(),
                    "error", "Invalid entity class: " + entityClass));
        }
    }

    public void showEntityAtRevision(Context ctx) {
        requireCan(ctx, "audit.read");
        String entityClass = ctx.queryParam("entityClass");
        Long entityId = parseLong(ctx.queryParam("entityId"));
        Long revision = parseLong(ctx.queryParam("revision"));

        if (entityClass == null || entityId == null || revision == null) {
            ctx.render("audit/detail", Map.of(
                    "user", currentUser(ctx),
                    "navSections", navSections(ctx),
                    "entry", null,
                    "error", "Missing parameters"));
            return;
        }

        try {
            Class<?> clazz = Class.forName(entityClass);
            var entry = auditQueryService.getEntityAtRevision(clazz, entityId, revision);
            ctx.render("audit/detail", Map.of(
                    "user", currentUser(ctx),
                    "navSections", navSections(ctx),
                    "entry", entry,
                    "error", null));
        } catch (ClassNotFoundException e) {
            ctx.render("audit/detail", Map.of(
                    "user", currentUser(ctx),
                    "navSections", navSections(ctx),
                    "entry", null,
                    "error", "Invalid entity class: " + entityClass));
        }
    }

    private Long parseLong(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}