-- ============================================================================
-- V19__catalog_seed.sql
-- Datos de prueba para el catálogo público: editions y stock items.
-- ============================================================================

-- Editions para los works existentes
INSERT INTO editions (edition_number, publisher_id, format_id, language_id, publication_year, pages, isbn, work_id, enabled) VALUES
    ('1st Edition', 1, 2, 1, 1967, 417, '978-0-06-088328-7', 1, TRUE),
    ('2nd Edition', 1, 2, 1, 1970, 420, '978-0-06-088329-4', 1, TRUE),
    ('1st Edition', 2, 2, 2, 1997, 223, '978-0-7475-3269-9', 2, TRUE),
    ('2nd Edition', 2, 1, 2, 1998, 223, '978-0-7475-3270-5', 2, TRUE),
    ('1st Edition', 1, 2, 2, 1949, 328, '978-0-452-28423-4', 3, TRUE),
    ('2nd Edition', 1, 2, 2, 1950, 330, '978-0-452-28424-1', 3, TRUE);

-- Edition authors
INSERT INTO edition_authors (edition_id, author_id, author_role_id, role, enabled, created_at, updated_at) VALUES
    (1, 1, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 1, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 2, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 2, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (5, 3, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (6, 3, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Stock items para las ediciones
INSERT INTO stock_items (code, edition_id, location_id, state, condition, enabled) VALUES
    ('STK-001', 1, 1, 'AVAILABLE', 'GOOD', TRUE),
    ('STK-002', 1, 1, 'AVAILABLE', 'GOOD', TRUE),
    ('STK-003', 1, 2, 'AVAILABLE', 'GOOD', TRUE),
    ('STK-004', 2, 1, 'AVAILABLE', 'GOOD', TRUE),
    ('STK-005', 3, 1, 'AVAILABLE', 'GOOD', TRUE),
    ('STK-006', 3, 2, 'AVAILABLE', 'GOOD', TRUE),
    ('STK-007', 3, 1, 'BORROWED', 'GOOD', TRUE),
    ('STK-008', 4, 2, 'AVAILABLE', 'GOOD', TRUE),
    ('STK-009', 5, 3, 'AVAILABLE', 'GOOD', TRUE),
    ('STK-010', 5, 3, 'AVAILABLE', 'GOOD', TRUE),
    ('STK-011', 6, 3, 'REVIEW', 'WORN', TRUE),
    ('STK-012', 6, 1, 'AVAILABLE', 'GOOD', TRUE);
