-- ============================================================================
-- V20__add_daily_price_to_stock.sql
-- Add daily price column to stock_items for reservation pricing.
-- ============================================================================

ALTER TABLE stock_items ADD COLUMN daily_price DECIMAL(10,2) DEFAULT 10.00;
UPDATE stock_items SET daily_price = 10.00 WHERE daily_price IS NULL;
ALTER TABLE stock_items ALTER COLUMN daily_price SET NOT NULL;

-- Update audit table
ALTER TABLE stock_items_AUD ADD COLUMN daily_price DECIMAL(10,2);
