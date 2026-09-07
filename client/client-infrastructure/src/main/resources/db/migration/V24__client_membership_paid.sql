-- ============================================================================
-- V24__client_membership_paid.sql
-- Add membership_paid flag to clients for membership fee tracking.
-- ============================================================================

ALTER TABLE clients ADD COLUMN membership_paid BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE clients_AUD ADD COLUMN membership_paid BOOLEAN;
