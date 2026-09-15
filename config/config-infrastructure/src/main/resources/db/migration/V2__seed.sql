-- ============================================================================
-- V2__seed.sql
-- Initial seed data for all modules.
-- ============================================================================

INSERT INTO modules (code, name, menu_label, icon, sort_order, enabled) VALUES
('dashboard', 'Dashboard', 'Dashboard', 'bi-speedometer2', 1, TRUE),
('iam', 'Identity & Access', 'Administration', 'bi-shield-lock', 2, TRUE),
('books', 'Books', 'Catalog', 'bi-book', 3, TRUE),
('stock', 'Stock', 'Stock', 'bi-box-seam', 4, TRUE),
('client', 'Clients', 'Clients', 'bi-people', 5, TRUE),
('reservation', 'Reservations', 'Reservations', 'bi-calendar-check', 6, TRUE),
('accounting', 'Accounting', 'Accounting', 'bi-cash-coin', 7, TRUE),
('config', 'Config', 'Config', 'bi-gear', 8, TRUE);

INSERT INTO permissions (code, name, menu_label, icon, url, sort_order, module_id) VALUES
-- Dashboard
('dashboard.view', 'View Dashboard', 'Dashboard', 'bi-speedometer2', '/', 1, 1),
-- IAM
('users.create', 'Create Users', NULL, NULL, NULL, 10, 2),
('users.read', 'View Users', 'Users', 'bi-people', '/iam/users', 11, 2),
('users.update', 'Edit Users', NULL, NULL, NULL, 12, 2),
('users.delete', 'Delete Users', NULL, NULL, NULL, 13, 2),
('users.reinstate', 'Reinstate Users', NULL, NULL, NULL, 14, 2),
('roles.create', 'Create Roles', NULL, NULL, NULL, 20, 2),
('roles.read', 'View Roles', 'Roles', 'bi-person-badge', '/iam/roles', 21, 2),
('roles.update', 'Edit Roles', NULL, NULL, NULL, 22, 2),
('roles.delete', 'Delete Roles', NULL, NULL, NULL, 23, 2),
('roles.reinstate', 'Reinstate Roles', NULL, NULL, NULL, 24, 2),
('permissions.create', 'Create Permissions', NULL, NULL, NULL, 30, 2),
('permissions.read', 'View Permissions', 'Permissions', 'bi-shield-check', '/iam/permissions', 31, 2),
('permissions.update', 'Edit Permissions', NULL, NULL, NULL, 32, 2),
('permissions.delete', 'Delete Permissions', NULL, NULL, NULL, 33, 2),
('modules.read', 'View Modules', 'Modules', 'bi-boxes', '/iam/modules', 40, 2),
('audit.read', 'View Audit', 'Audit', 'bi-journal-check', '/iam/audit', 50, 2),
('notifications.stream', 'View Notifications', 'Notifications', 'bi-bell', NULL, 60, 2),
-- Books
('works.create', 'Create Works', NULL, NULL, NULL, 110, 3),
('works.read', 'View Works', 'Works', 'bi-book', '/books/works', 111, 3),
('works.update', 'Edit Works', NULL, NULL, NULL, 112, 3),
('works.delete', 'Delete Works', NULL, NULL, NULL, 113, 3),
('works.reactivate', 'Reactivate Works', NULL, NULL, NULL, 114, 3),
('editions.create', 'Create Editions', NULL, NULL, NULL, 210, 3),
('editions.read', 'View Editions', 'Editions', 'bi-journal-bookmark', '/books/editions', 211, 3),
('editions.update', 'Edit Editions', NULL, NULL, NULL, 212, 3),
('editions.delete', 'Delete Editions', NULL, NULL, NULL, 213, 3),
('editions.reactivate', 'Reactivate Editions', NULL, NULL, NULL, 214, 3),
('authors.create', 'Create Authors', NULL, NULL, NULL, 310, 3),
('authors.read', 'View Authors', 'Authors', 'bi-person', '/books/authors', 311, 3),
('authors.update', 'Edit Authors', NULL, NULL, NULL, 312, 3),
('authors.delete', 'Delete Authors', NULL, NULL, NULL, 313, 3),
('authors.reactivate', 'Reactivate Authors', NULL, NULL, NULL, 314, 3),
('publishers.create', 'Create Publishers', NULL, NULL, NULL, 410, 3),
('publishers.read', 'View Publishers', 'Publishers', 'bi-building', '/books/publishers', 411, 3),
('publishers.update', 'Edit Publishers', NULL, NULL, NULL, 412, 3),
('publishers.delete', 'Delete Publishers', NULL, NULL, NULL, 413, 3),
('publishers.reactivate', 'Reactivate Publishers', NULL, NULL, NULL, 414, 3),
('languages.create', 'Create Languages', NULL, NULL, NULL, 510, 3),
('languages.read', 'View Languages', 'Languages', 'bi-translate', '/books/languages', 511, 3),
('languages.update', 'Edit Languages', NULL, NULL, NULL, 512, 3),
('languages.delete', 'Delete Languages', NULL, NULL, NULL, 513, 3),
('languages.reactivate', 'Reactivate Languages', NULL, NULL, NULL, 514, 3),
('formats.create', 'Create Formats', NULL, NULL, NULL, 610, 3),
('formats.read', 'View Formats', 'Formats', 'bi-file-earmark', '/books/formats', 611, 3),
('formats.update', 'Edit Formats', NULL, NULL, NULL, 612, 3),
('formats.delete', 'Delete Formats', NULL, NULL, NULL, 613, 3),
('formats.reactivate', 'Reactivate Formats', NULL, NULL, NULL, 614, 3),
('categories.create', 'Create Categories', NULL, NULL, NULL, 710, 3),
('categories.read', 'View Categories', 'Categories', 'bi-folder', '/books/categories', 711, 3),
('categories.update', 'Edit Categories', NULL, NULL, NULL, 712, 3),
('categories.delete', 'Delete Categories', NULL, NULL, NULL, 713, 3),
('categories.reactivate', 'Reactivate Categories', NULL, NULL, NULL, 714, 3),
('authorRoles.create', 'Create Author Roles', NULL, NULL, NULL, 710, 3),
('authorRoles.read', 'View Author Roles', 'Author Roles', 'bi-person-badge', '/books/authorRoles', 711, 3),
('authorRoles.update', 'Edit Author Roles', NULL, NULL, NULL, 712, 3),
('authorRoles.delete', 'Delete Author Roles', NULL, NULL, NULL, 713, 3),
('authorRoles.reactivate', 'Reactivate Author Roles', NULL, NULL, NULL, 714, 3),
-- Stock
('stock.create', 'Create Stock Items', NULL, NULL, NULL, 10, 4),
('stock.read', 'View Stock Items', 'Stock Items', 'bi-box-seam', '/stock/items', 11, 4),
('stock.update', 'Edit Stock Items', NULL, NULL, NULL, 12, 4),
('stock.delete', 'Delete Stock Items', NULL, NULL, NULL, 13, 4),
('stock.reactivate', 'Reactivate Stock Items', NULL, NULL, NULL, 14, 4),
('stocklocations.create', 'Create Locations', NULL, NULL, NULL, 20, 4),
('stocklocations.read', 'View Locations', 'Locations', 'bi-geo-alt', '/stock/locations', 21, 4),
('stocklocations.update', 'Edit Locations', NULL, NULL, NULL, 22, 4),
('stocklocations.delete', 'Delete Locations', NULL, NULL, NULL, 23, 4),
('stocklocations.reactivate', 'Reactivate Locations', NULL, NULL, NULL, 24, 4),
('stockmovements.read', 'View Movements', 'Movements', 'bi-arrow-left-right', '/stock/movements', 30, 4),
-- Client
('clients.create', 'Create Clients', NULL, NULL, NULL, 10, 5),
('clients.read', 'View Clients', 'Clients', 'bi-people', '/clients', 11, 5),
('clients.update', 'Edit Clients', NULL, NULL, NULL, 12, 5),
('clients.delete', 'Delete Clients', NULL, NULL, NULL, 13, 5),
('clients.reactivate', 'Reactivate Clients', NULL, NULL, NULL, 14, 5),
('clients.upgrade', 'Upgrade Clients', NULL, NULL, NULL, 15, 5),
-- Reservation
('reservations.create', 'Create Reservations', NULL, NULL, NULL, 10, 6),
('reservations.read', 'View Reservations', 'Reservations', 'bi-calendar-check', '/reservations', 11, 6),
('reservations.update', 'Edit Reservations', NULL, NULL, NULL, 12, 6),
('reservations.delete', 'Delete Reservations', NULL, NULL, NULL, 13, 6),
('reservations.cancel', 'Cancel Reservations', NULL, NULL, NULL, 14, 6),
('reservations.fulfill', 'Fulfill Reservations', NULL, NULL, NULL, 15, 6),
('reservations.return', 'Process Returns', NULL, NULL, NULL, 16, 6),
('reservations.renew', 'Renew Reservations', NULL, NULL, NULL, 17, 6),
('reservations.expire', 'Expire Reservations', NULL, NULL, NULL, 18, 6),
-- Accounting
('accounting.payments.create', 'Record Payment', 'Payments', 'bi-cash', '/accounting/payments', 10, 7),
('accounting.payments.read', 'View Payments', NULL, NULL, NULL, 11, 7),
('accounting.payroll.create', 'Record Payroll', 'Payroll', 'bi-cash-stack', '/accounting/payroll', 20, 7),
('accounting.payroll.read', 'View Payroll', NULL, NULL, NULL, 21, 7),
('accounting.expenses.create', 'Record Expense', 'Expenses', 'bi-receipt', '/accounting/expenses', 30, 7),
('accounting.expenses.read', 'View Expenses', NULL, NULL, NULL, 31, 7),
('accounting.balance.read', 'View Balance', 'Balance', 'bi-bar-chart', '/accounting/balance', 40, 7),
('accounting.paymentmethods.create', 'Create Payment Methods', NULL, NULL, NULL, 51, 7),
('accounting.paymentmethods.read', 'View Payment Methods', 'Payment Methods', 'bi-credit-card', '/accounting/payment-methods', 50, 7),
('accounting.paymentmethods.delete', 'Delete Payment Methods', NULL, NULL, NULL, 52, 7),
-- Config
('config.read', 'View Config', 'Config', 'bi-gear', NULL, 10, 8),
('config.update', 'Update Config', NULL, NULL, NULL, 11, 8);

INSERT INTO roles (name, description) VALUES
('ADMIN', 'Full access to all modules'),
('EMPLOYEE', 'Library staff: manages catalog and daily operations');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r CROSS JOIN permissions p WHERE r.name = 'ADMIN';

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id FROM roles r, permissions p
WHERE r.name = 'EMPLOYEE'
  AND p.code IN ('dashboard.view', 'users.read', 'roles.read', 'permissions.read', 'modules.read', 'audit.read',
    'works.read', 'editions.read', 'authors.read', 'publishers.read', 'languages.read', 'formats.read',
    'categories.read', 'authorRoles.read', 'stock.read', 'stocklocations.read', 'stockmovements.read',
    'clients.read', 'clients.create', 'clients.update', 'clients.upgrade',
    'reservations.read', 'reservations.create', 'reservations.fulfill', 'reservations.return', 'reservations.renew', 'reservations.expire',
    'accounting.payments.read', 'accounting.payments.create',
    'accounting.payroll.read', 'accounting.payroll.create',
    'accounting.expenses.read', 'accounting.expenses.create',
    'accounting.paymentmethods.read',
    'accounting.balance.read', 'notifications.stream');

-- Users (password: Admin123!)
INSERT INTO users (username, password, full_name, email, enabled, role_id)
SELECT 'admin', '$argon2id$v=19$m=65536,t=3,p=4$R2FpdGVzdZWNyZXQ$R2FpdGVzdZWNyZXQ', 'Administrator', 'admin@library.local', TRUE, r.id
FROM roles r WHERE r.name = 'ADMIN';

INSERT INTO users (username, password, full_name, email, enabled, role_id)
SELECT 'employee', '$argon2id$v=19$m=65536,t=3,p=4$R2FpdGVzdZWNyZXQ$R2FpdGVzdZWNyZXQ', 'Library Employee', 'employee@library.local', TRUE, r.id
FROM roles r WHERE r.name = 'EMPLOYEE';

-- Author Roles
INSERT INTO author_roles (code, name, description, created_at, updated_at) VALUES
('ROLE-ldAu4kQ7', 'Lead Author', 'Primary author', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROLE-coAu9mP2', 'Co-Author', 'Contributing author', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROLE-edtR3xV8', 'Editor', 'Editor', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROLE-trlS7nB4', 'Translator', 'Translator', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ROLE-conT2wQ5', 'Contributor', 'Contributor', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Languages
INSERT INTO languages (code, name, enabled) VALUES
('LANG-spaXx5qR', 'Spanish', TRUE),
('LANG-engK7mPQ', 'English', TRUE),
('LANG-fraT2wQx', 'French', TRUE),
('LANG-gerY9nBx', 'German', TRUE),
('LANG-porM4vLp', 'Portuguese', TRUE);

-- Book Formats
INSERT INTO book_formats (code, name, description, enabled) VALUES
('FMT-hrdcVR6s', 'Hardcover', 'Hardcover edition', TRUE),
('FMT-papb8XwQ', 'Paperback', 'Paperback edition', TRUE),
('FMT-ebkJM3nL', 'E-book', 'Digital edition', TRUE),
('FMT-audK9p2r', 'Audiobook', 'Audio edition', TRUE);

-- Publishers
INSERT INTO publishers (name, country, website, enabled) VALUES
('Penguin Random House', 'USA', 'https://www.penguinrandomhouse.com', TRUE),
('HarperCollins', 'USA', 'https://www.harpercollins.com', TRUE),
('Planeta', 'Spain', 'https://www.planeta.es', TRUE);

-- Authors
INSERT INTO authors (first_name, last_name, biography, birth_date, death_date, enabled) VALUES
('Gabriel', 'Garcia Marquez', 'Colombian novelist', '1927-03-06', '2014-04-17', TRUE),
('J.K.', 'Rowling', 'British author', '1965-07-31', NULL, TRUE),
('George', 'Orwell', 'English novelist', '1903-06-25', '1950-01-21', TRUE);

-- Categories
INSERT INTO categories (code, name, description, enabled) VALUES
('CAT-ficH7rQ2', 'Fiction', 'Fiction books', TRUE),
('CAT-nficP4bK', 'Non-Fiction', 'Non-fiction books', TRUE),
('CAT-sciQ8mXv', 'Science', 'Science books', TRUE),
('CAT-hisJ2wPn', 'History', 'History books', TRUE);

-- Works
INSERT INTO works (title, subtitle, original_language_id, category_id, summary, enabled) VALUES
('One Hundred Years of Solitude', NULL, 1, 1, 'Multi-generational saga of the Buendia family.', TRUE),
('Harry Potter and the Philosopher''s Stone', NULL, 2, 1, 'A young wizard begins his journey.', TRUE),
('1984', NULL, 2, 1, 'Dystopian social science fiction.', TRUE);

-- Editions
INSERT INTO editions (edition_number, publisher_id, format_id, language_id, publication_year, pages, isbn, work_id, enabled) VALUES
('1st Edition', 1, 2, 1, 1967, 417, '978-0-06-088328-7', 1, TRUE),
('2nd Edition', 1, 2, 1, 1970, 420, '978-0-06-088329-4', 1, TRUE),
('1st Edition', 2, 2, 2, 1997, 223, '978-0-7475-3269-9', 2, TRUE),
('2nd Edition', 2, 1, 2, 1998, 223, '978-0-7475-3270-5', 2, TRUE),
('1st Edition', 1, 2, 2, 1949, 328, '978-0-452-28423-4', 3, TRUE),
('2nd Edition', 1, 2, 2, 1950, 330, '978-0-452-28424-1', 3, TRUE);

-- Edition Authors
INSERT INTO edition_authors (edition_id, author_id, author_role_id, role, enabled, created_at, updated_at) VALUES
(1, 1, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 1, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 2, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 2, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 3, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 3, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Work Authors
INSERT INTO work_authors (work_id, author_id, author_role_id, role, enabled, created_at, updated_at) VALUES
(1, 1, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 2, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 3, (SELECT id FROM author_roles WHERE code = 'ROLE-ldAu4kQ7'), 'Author', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Stock Locations
INSERT INTO stock_locations (code, name, description, capacity, enabled) VALUES
('SHLF-A1', 'Shelf A1', 'Fiction - Section A1', 100, TRUE),
('SHLF-A2', 'Shelf A2', 'Fiction - Section A2', 100, TRUE),
('SHLF-B1', 'Shelf B1', 'Non-Fiction - Section B1', 80, TRUE),
('RESERVE', 'Reserved Shelf', 'New arrivals and reserved items', 50, TRUE);

-- Stock Items
INSERT INTO stock_items (code, edition_id, location_id, state, condition, daily_price, enabled) VALUES
('STK-001', 1, 1, 'AVAILABLE', 'GOOD', 10.00, TRUE),
('STK-002', 1, 1, 'AVAILABLE', 'GOOD', 10.00, TRUE),
('STK-003', 1, 2, 'AVAILABLE', 'GOOD', 10.00, TRUE),
('STK-004', 2, 1, 'AVAILABLE', 'GOOD', 10.00, TRUE),
('STK-005', 3, 1, 'AVAILABLE', 'GOOD', 10.00, TRUE),
('STK-006', 3, 2, 'AVAILABLE', 'GOOD', 10.00, TRUE),
('STK-007', 3, 1, 'BORROWED', 'GOOD', 10.00, TRUE),
('STK-008', 4, 2, 'AVAILABLE', 'GOOD', 10.00, TRUE),
('STK-009', 5, 3, 'AVAILABLE', 'GOOD', 10.00, TRUE),
('STK-010', 5, 3, 'AVAILABLE', 'GOOD', 10.00, TRUE),
('STK-011', 6, 3, 'REVIEW', 'WORN', 10.00, TRUE),
('STK-012', 6, 1, 'AVAILABLE', 'GOOD', 10.00, TRUE);

-- Payment Methods
INSERT INTO payment_methods (code, name, enabled, created_at, updated_at) VALUES
('CASH', 'Cash', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('CARD', 'Credit/Debit Card', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TRANSFER', 'Bank Transfer', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('OTHER', 'Other', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Accounts
INSERT INTO accounts (code, name, type, description, enabled, created_at, updated_at) VALUES
('ACC-1000', 'Cash on Hand', 'CASH', 'Physical cash received', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),('ACC-1100', 'Bank Account', 'BANK', 'Bank transfers', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-1200', 'Accounts Receivable', 'RECEIVABLE', 'Customer receivables', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-2000', 'Accounts Payable', 'PAYABLE', 'Vendor payables', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-2100', 'Payroll Payable', 'PAYROLL', 'Payroll payable', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-3000', 'General Income', 'INCOME', 'General income', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-3100', 'Rental Income', 'INCOME', 'Rental income', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-3200', 'Membership Income', 'INCOME', 'Membership income', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-4000', 'General Expenses', 'EXPENSE', 'General expenses', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-5000', 'Late Fee Income', 'INCOME', 'Late fees', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-6000', 'Deposits Liability', 'LIABILITY', 'Deposits held', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-7000', 'Refunds', 'REFUND', 'Refunds', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ACC-8000', 'Owner Equity', 'EQUITY', 'Owner equity', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Unique constraints for books
ALTER TABLE work_authors ADD CONSTRAINT uq_work_authors_work_author UNIQUE (work_id, author_id);
ALTER TABLE edition_authors ADD CONSTRAINT uq_edition_authors_edition_author UNIQUE (edition_id, author_id);
ALTER TABLE editions ADD CONSTRAINT uq_editions_work_number UNIQUE (work_id, edition_number);

-- Indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_role_id ON users(role_id);
CREATE INDEX idx_works_title ON works(title);
CREATE INDEX idx_editions_work_id ON editions(work_id);
CREATE INDEX idx_editions_isbn ON editions(isbn);
CREATE INDEX idx_authors_full_name ON authors(first_name, last_name);
CREATE INDEX idx_categories_name ON categories(name);
CREATE INDEX idx_publishers_name ON publishers(name);
CREATE INDEX idx_book_formats_name ON book_formats(name);
CREATE INDEX idx_languages_code ON languages(code);
CREATE INDEX idx_stock_items_edition_id ON stock_items(edition_id);
CREATE INDEX idx_stock_items_state ON stock_items(state);
CREATE INDEX idx_stock_items_location_id ON stock_items(location_id);
CREATE INDEX idx_stock_items_code ON stock_items(code);
CREATE INDEX idx_stock_movements_stock_item_id ON stock_movements(stock_item_id);
CREATE INDEX idx_clients_dni ON clients(dni);
CREATE INDEX idx_clients_code ON clients(code);
CREATE INDEX idx_clients_type ON clients(type);
CREATE INDEX idx_reservations_client_id ON reservations(client_id);
CREATE INDEX idx_reservations_status ON reservations(status);
CREATE INDEX idx_reservations_due_date ON reservations(due_date);
CREATE INDEX idx_reservations_stock_item_id ON reservations(stock_item_id);
CREATE INDEX idx_reservations_created_at ON reservations(created_at);
CREATE INDEX idx_accounts_code ON accounts(code);
CREATE INDEX idx_payments_code ON payments(code);
CREATE INDEX idx_payments_date ON payments(payment_date);
CREATE INDEX idx_payments_method ON payments(payment_method);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_payroll_code ON payroll_payments(code);
CREATE INDEX idx_payroll_date ON payroll_payments(payment_date);
CREATE INDEX idx_expenses_code ON expenses(code);
CREATE INDEX idx_expenses_date ON expenses(expense_date);
CREATE INDEX idx_expenses_category ON expenses(category);
CREATE INDEX idx_payment_methods_code ON payment_methods(code);
CREATE INDEX idx_refunds_code ON refunds(code);

