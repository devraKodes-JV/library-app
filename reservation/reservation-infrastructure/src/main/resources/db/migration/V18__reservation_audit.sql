-- ============================================================================
-- V18__reservation_audit.sql
-- Agregar reservation_id a la tabla de auditoría de stock_items.
-- ============================================================================

-- Agregar reservation_id a la tabla de auditoría de stock_items
ALTER TABLE stock_items_AUD ADD COLUMN reservation_id BIGINT;
