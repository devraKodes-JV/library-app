-- ============================================================================
-- V23__accounting_permissions.sql
-- Register accounting module and its permissions.
-- ============================================================================

INSERT INTO modules (code, name, menu_label, icon, sort_order, enabled) VALUES
    ('accounting', 'Accounting', 'Accounting', 'bi-cash-coin', 7, TRUE);

INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id) VALUES
    ('accounting.payments.create', 'Record Payment',    'Payments',  'bi-cash',           '/accounting/payments', 10, 7),
    ('accounting.payments.read',   'View Payments',     NULL,        NULL,                NULL,                     11, 7),
    ('accounting.payroll.create',  'Record Payroll',    'Payroll',   'bi-cash-stack',     '/accounting/payroll',  12, 7),
    ('accounting.payroll.read',    'View Payroll',      NULL,        NULL,                NULL,                     13, 7),
    ('accounting.expenses.create', 'Record Expense',    'Expenses',  'bi-receipt',        '/accounting/expenses', 14, 7),
    ('accounting.expenses.read',   'View Expenses',     NULL,        NULL,                NULL,                     15, 7);

-- Grant all to ADMIN
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p
WHERE r.name = 'ADMIN' AND p.module_id = 7;

-- Grant create+read to EMPLOYEE
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'EMPLOYEE' AND p.code IN
    ('accounting.payments.create', 'accounting.payments.read',
     'accounting.payroll.create',  'accounting.payroll.read',
     'accounting.expenses.create', 'accounting.expenses.read');
