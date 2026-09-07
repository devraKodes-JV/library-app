-- ============================================================================
-- V22__accounting_seed.sql
-- Seed chart of accounts with default accounts.
-- ============================================================================

INSERT INTO accounts (code, name, type, description, enabled, created_at, updated_at) VALUES
    -- Assets
    ('ACC-1000', 'Cash on Hand',       'ASSET',    'Physical cash received',          TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-1100', 'Bank Account',       'ASSET',    'Bank transfers',                  TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Liabilities
    ('ACC-2000', 'Deposit Liability',  'LIABILITY', 'Customer deposits held',          TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-2100', 'Membership Liability', 'LIABILITY', 'Pre-paid memberships',          TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Income
    ('ACC-3000', 'Loan Income',        'INCOME',   'Income from book loans',          TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-3100', 'Late Fee Income',    'INCOME',   'Income from late fees',           TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-3200', 'Membership Income',  'INCOME',   'Income from memberships',         TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-3300', 'Damage Fee Income',  'INCOME',   'Income from damage charges',      TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    -- Expenses
    ('ACC-4000', 'Salaries Expense',   'EXPENSE',  'Employee salary payments',        TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-4100', 'Rent Expense',       'EXPENSE',  'Premises rent',                   TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-4200', 'Utilities Expense',  'EXPENSE',  'Electricity, water, internet',    TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-4300', 'Maintenance Expense', 'EXPENSE', 'Repairs and maintenance',         TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-4400', 'Supplies Expense',   'EXPENSE',  'Office and library supplies',     TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-4500', 'Marketing Expense',  'EXPENSE',  'Marketing and advertising',       TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('ACC-4900', 'Other Expenses',     'EXPENSE',  'Miscellaneous expenses',          TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
