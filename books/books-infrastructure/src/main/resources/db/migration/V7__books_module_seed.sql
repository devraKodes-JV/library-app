-- ============================================================================
-- V7__books_module_seed.sql
-- Modulo Books: modulo, permisos CRUD y grants para ADMIN.
-- ============================================================================

INSERT INTO modules (code, name, menu_label, icon, sort_order, enabled)
VALUES ('books', 'Books', 'Catalog', 'bi-book', 3, TRUE);

INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id) VALUES
    -- Works
    ('works.create',  'Create Works',     NULL,               NULL,    NULL,   110, 3),
    ('works.read',    'View Works',       'Works',            'bi-book',            '/books/works',   111, 3),
    ('works.update',  'Edit Works',       NULL,               NULL,    NULL,   112, 3),
    ('works.delete',  'Delete Works',     NULL,               NULL,    NULL,   113, 3),
    -- Editions
    ('editions.create', 'Create Editions', NULL,              NULL,    NULL,   210, 3),
    ('editions.read',   'View Editions',   'Editions',       'bi-journal-bookmark','/books/editions',211, 3),
    ('editions.update', 'Edit Editions',   NULL,              NULL,    NULL,   212, 3),
    ('editions.delete', 'Delete Editions', NULL,              NULL,    NULL,   213, 3),
    -- Authors
    ('authors.create',  'Create Authors',  NULL,              NULL,    NULL,   310, 3),
    ('authors.read',    'View Authors',    'Authors',        'bi-person',         '/books/authors',   311, 3),
    ('authors.update',  'Edit Authors',    NULL,              NULL,    NULL,   312, 3),
    ('authors.delete',  'Delete Authors',  NULL,              NULL,    NULL,   313, 3),
    -- Publishers
    ('publishers.create', 'Create Publishers', NULL,           NULL,    NULL,   410, 3),
    ('publishers.read',   'View Publishers',   'Publishers',  'bi-building',       '/books/publishers',411, 3),
    ('publishers.update', 'Edit Publishers',   NULL,           NULL,    NULL,   412, 3),
    ('publishers.delete', 'Delete Publishers', NULL,           NULL,    NULL,   413, 3),
    -- Languages
    ('languages.create',  'Create Languages',  NULL,           NULL,    NULL,   510, 3),
    ('languages.read',    'View Languages',    'Languages',   'bi-translate',      '/books/languages', 511, 3),
    ('languages.update',  'Edit Languages',    NULL,           NULL,    NULL,   512, 3),
    ('languages.delete',  'Delete Languages',  NULL,           NULL,    NULL,   513, 3),
    -- Formats
    ('formats.create',  'Create Formats',    NULL,             NULL,    NULL,   610, 3),
    ('formats.read',    'View Formats',      'Formats',        'bi-bookmark',       '/books/formats',   611, 3),
    ('formats.update',  'Edit Formats',      NULL,             NULL,    NULL,   612, 3),
    ('formats.delete',  'Delete Formats',    NULL,             NULL,    NULL,   613, 3),
    -- Categories
    ('categories.create', 'Create Categories', NULL,            NULL,    NULL,   710, 3),
    ('categories.read',   'View Categories',   'Categories',   'bi-folder',         '/books/categories',711, 3),
    ('categories.update', 'Edit Categories',   NULL,            NULL,    NULL,   712, 3),
    ('categories.delete', 'Delete Categories', NULL,            NULL,    NULL,   713, 3);

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r CROSS JOIN permissions p
WHERE r.name = 'ADMIN'
  AND p.module_id = 3;
