-- ============================================================================
-- V4__settings.sql
-- Default system settings.
-- ============================================================================

INSERT INTO settings (module, setting_key, setting_value, setting_type, description, updated_at, updated_by) VALUES
('global', 'app.date.format', 'dd/MM/yyyy', 'DATE_FORMAT', 'UI date format', CURRENT_TIMESTAMP, 'system'),

('client', 'membership.fee.monthly', '50.00', 'DECIMAL', 'Monthly membership fee', CURRENT_TIMESTAMP, 'system'),
('client', 'membership.fee.yearly', '500.00', 'DECIMAL', 'Yearly membership fee', CURRENT_TIMESTAMP, 'system'),
('client', 'membership.default.duration.months', '12', 'INTEGER', 'Default membership duration in months', CURRENT_TIMESTAMP, 'system'),
('client', 'membership.casual.loan.days', '7', 'INTEGER', 'Loan days for CASUAL clients', CURRENT_TIMESTAMP, 'system'),
('client', 'membership.member.loan.days', '21', 'INTEGER', 'Loan days for MEMBER clients', CURRENT_TIMESTAMP, 'system'),
('client', 'membership.casual.max.renewals', '1', 'INTEGER', 'Max renewals for CASUAL', CURRENT_TIMESTAMP, 'system'),
('client', 'membership.member.max.renewals', '3', 'INTEGER', 'Max renewals for MEMBER', CURRENT_TIMESTAMP, 'system'),
('client', 'membership.casual.max.active', '3', 'INTEGER', 'Max active reservations for CASUAL', CURRENT_TIMESTAMP, 'system'),
('client', 'membership.member.max.active', '5', 'INTEGER', 'Max active reservations for MEMBER', CURRENT_TIMESTAMP, 'system'),
('client', 'membership.auto.demote.enabled', 'true', 'BOOLEAN', 'Auto-demote expired members to CASUAL', CURRENT_TIMESTAMP, 'system'),
('client', 'membership.deposit.percentage', '50', 'INTEGER', 'Deposit percentage for membership', CURRENT_TIMESTAMP, 'system'),

('reservation', 'deposit.percentage', '50', 'INTEGER', 'Default deposit percentage', CURRENT_TIMESTAMP, 'system'),
('reservation', 'late.fee.per.day', '1.00', 'DECIMAL', 'Late fee per day', CURRENT_TIMESTAMP, 'system'),
('reservation', 'pickup.deadline.hours', '48', 'INTEGER', 'Hours to pick up after reservation', CURRENT_TIMESTAMP, 'system'),
('reservation', 'default.loan.days', '7', 'INTEGER', 'Default loan days when creating reservation', CURRENT_TIMESTAMP, 'system'),
('reservation', 'max.renewals', '1', 'INTEGER', 'Default max renewals', CURRENT_TIMESTAMP, 'system'),
('reservation', 'cancellation.fee.percentage', '0', 'INTEGER', 'Cancellation fee percentage', CURRENT_TIMESTAMP, 'system'),

('accounting', 'currency', 'USD', 'STRING', 'Default currency', CURRENT_TIMESTAMP, 'system'),
('accounting', 'late.fee.enabled', 'true', 'BOOLEAN', 'Enable late fees', CURRENT_TIMESTAMP, 'system');
