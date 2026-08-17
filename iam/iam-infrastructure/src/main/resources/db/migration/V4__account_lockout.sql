-- ============================================================================
-- V4__account_lockout.sql
-- Add account lockout columns to users table for brute force protection.
-- ============================================================================

ALTER TABLE users ADD COLUMN IF NOT EXISTS failed_login_attempts INTEGER NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN IF NOT EXISTS locked_until TIMESTAMP(6) WITH TIME ZONE;

ALTER TABLE users_AUD ADD COLUMN IF NOT EXISTS failed_login_attempts INTEGER;
ALTER TABLE users_AUD ADD COLUMN IF NOT EXISTS locked_until TIMESTAMP(6) WITH TIME ZONE;
