-- ============================================================================
-- V2__seed.sql  (CONSOLIDADO - reemplaza a los antiguos V2, V4, V6, V7, V8)
-- Datos iniciales para la aplicación Library Ultimate.
--
-- Crea:
--   * Módulos: dashboard, iam
--   * Permisos CRUD finos (roles/users/permissions), dashboard.view,
--     notifications.stream, users.reinstate, modules.read
--   * Roles: ADMIN, EMPLOYEE
--   * Grants: ADMIN -> todos los permisos; EMPLOYEE -> dashboard.view
--   * Usuarios: 2 administradores (admin, admin2) + 1 empleado (employee),
--     con sus hashes Argon2id reales (NO placeholder).
--
-- Credenciales por defecto:
--   admin    / Admin123!     (ADMIN)
--   admin2   / Admin123!     (ADMIN)
--   employee / Employee123!  (EMPLOYEE)
--
-- Los hashes se generaron con Argon2id (m=19456, t=2, p=1), los mismos
-- parámetros que usa BouncyCastleArgon2PasswordHasher en la aplicación.
-- ============================================================================

-- Módulos ---------------------------------------------------------------------
INSERT INTO modules (code, name, menu_label, icon, sort_order, enabled) VALUES
    ('dashboard', 'Dashboard', 'Dashboard', 'bi-speedometer2', 1, TRUE),
    ('iam',       'Identity & Access', 'Administration', 'bi-shield-lock', 2, TRUE);

-- Permisos --------------------------------------------------------------------
-- Dashboard
INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id) VALUES
    ('dashboard.view', 'View Dashboard', 'Dashboard', 'bi-speedometer2', '/', 1, 1);

-- IAM: roles (CRUD)
INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id) VALUES
    ('roles.create',  'Create Roles',   NULL,               NULL,              NULL, 10, 2),
    ('roles.read',    'View Roles',     'Roles',            'bi-person-badge', '/iam/roles', 11, 2),
    ('roles.update',  'Edit Roles',     NULL,               NULL,              NULL, 12, 2),
    ('roles.delete',  'Delete Roles',   NULL,               NULL,              NULL, 13, 2);

-- IAM: users (CRUD)
INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id) VALUES
    ('users.create',  'Create Users',   NULL,               NULL,              NULL, 20, 2),
    ('users.read',    'View Users',     'Users',            'bi-people',       '/iam/users', 21, 2),
    ('users.update',  'Edit Users',     NULL,               NULL,              NULL, 22, 2),
    ('users.delete',  'Delete Users',   NULL,               NULL,              NULL, 23, 2),
    ('users.reinstate', 'Reinstate Users', NULL,            NULL,              NULL, 24, 2);

-- IAM: permissions (read-only view)
INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id) VALUES
    ('permissions.create', 'Create Permissions', NULL, NULL, NULL, 30, 2),
    ('permissions.read',   'View Permissions',   'Permissions', 'bi-shield-check', '/iam/permissions', 31, 2),
    ('permissions.update', 'Edit Permissions',   NULL, NULL, NULL, 32, 2),
    ('permissions.delete', 'Delete Permissions', NULL, NULL, NULL, 33, 2);

-- IAM: modules (read-only)
INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id) VALUES
    ('modules.read', 'View Modules', 'Modules', 'bi-boxes', '/iam/modules', 40, 2);

-- IAM: notifications (SSE stream)
INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id) VALUES
    ('notifications.stream', 'View Notifications', 'Notifications', 'bi-bell', NULL, 50, 2);

-- Roles ----------------------------------------------------------------------
-- ADMIN: acceso total a todos los módulos y funciones de administración.
INSERT INTO roles (name, description) VALUES
    ('ADMIN', 'Full access to all modules and administration functions');

-- EMPLOYEE: solo dashboard (sin administración IAM).
INSERT INTO roles (name, description) VALUES
    ('EMPLOYEE', 'Library staff: manages catalog and daily operations');

-- Role <-> Permission mappings ------------------------------------------------
-- ADMIN recibe TODOS los permisos.
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r CROSS JOIN permissions p
WHERE r.name = 'ADMIN';

-- EMPLOYEE recibe solo el dashboard.
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'EMPLOYEE' AND p.code = 'dashboard.view';

-- Usuarios -------------------------------------------------------------------
-- admin / Admin123!  (ADMIN)
INSERT INTO users (username, password, full_name, email, enabled, role_id)
SELECT 'admin', '$argon2id$v=19$m=19456,t=2,p=1$p5tgUqjfihXtv4mECDGt0g==$KW7GYi5SO9CmpTLdRuTMzpSNjXIzqzYBEM67JECoc4w=',
       'Administrator', 'admin@library.local', TRUE, r.id
FROM roles r
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- admin2 / Admin123!  (ADMIN)
INSERT INTO users (username, password, full_name, email, enabled, role_id)
SELECT 'admin2', '$argon2id$v=19$m=19456,t=2,p=1$JUsNJs8qg/SkfcKpk6rtLg==$2StNs3BxV4ZuPC5WRbe4sFCRM0LnMJt/oc7gt4KvAgw=',
       'Second Administrator', 'admin2@library.local', TRUE, r.id
FROM roles r
WHERE r.name = 'ADMIN'
  AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin2');

-- employee / Employee123!  (EMPLOYEE)
INSERT INTO users (username, password, full_name, email, enabled, role_id)
SELECT 'employee', '$argon2id$v=19$m=19456,t=2,p=1$bET5dzK6pCM3It8caki3rg==$2OqdEGh4vGDmqb3j2O1S+cK6TzdyfCBvItZWF35aBSg=',
       'Library Employee', 'employee@library.local', TRUE, r.id
FROM roles r
WHERE r.name = 'EMPLOYEE'
  AND NOT EXISTS (SELECT 1 FROM users WHERE username = 'employee');
