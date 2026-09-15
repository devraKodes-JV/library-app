-- ============================================================================
-- V3__indexes.sql
-- Additional indexes, unique constraints, and module registrations.
-- ============================================================================

-- Composite indexes
CREATE INDEX idx_works_category_created ON works (category_id, created_at);
CREATE INDEX idx_works_language_created ON works (original_language_id, created_at);
CREATE INDEX idx_editions_work_number ON editions (work_id, edition_number);
