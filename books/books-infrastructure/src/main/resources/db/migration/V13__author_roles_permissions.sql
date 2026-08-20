-- ============================================================================
-- V13__author_roles_permissions.sql
-- Permissions for Author Roles management.
-- ============================================================================

INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id) VALUES
    ('authorRoles.create', 'Create Author Roles', NULL, NULL, NULL, 710, 3),
    ('authorRoles.read',   'View Author Roles',   'Author Roles', 'bi-person-badge', '/books/authorRoles', 711, 3),
    ('authorRoles.update', 'Edit Author Roles',   NULL, NULL, NULL, 712, 3),
    ('authorRoles.delete', 'Delete Author Roles', NULL, NULL, NULL, 713, 3);

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r CROSS JOIN permissions p
WHERE r.name = 'ADMIN'
  AND p.code LIKE 'authorRoles.%';
