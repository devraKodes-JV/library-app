-- ============================================================================
-- V6__reservation_daily_price.sql
-- Adds daily_price column to reservations table (and its audit table).
-- ============================================================================

ALTER TABLE reservations ADD COLUMN daily_price DECIMAL(10,2);

ALTER TABLE reservations_AUD ADD COLUMN daily_price DECIMAL(10,2);