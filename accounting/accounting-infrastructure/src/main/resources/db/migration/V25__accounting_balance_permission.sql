-- V25__accounting_balance_permission.sql
-- Adds a dedicated permission for the accounting balance view (read-only).

INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id, enabled, created_at)
VALUES ('accounting.balance.read', 'View Balance', 'Balance', 'bi bi-bar-chart', '/accounting/balance', 90, 7, TRUE, CURRENT_TIMESTAMP);

INSERT INTO role_permissions (role_id, permission_id)
SELECT 1, LASTVAL() FROM DUAL;

INSERT INTO role_permissions (role_id, permission_id)
SELECT 2, LASTVAL() FROM DUAL;
