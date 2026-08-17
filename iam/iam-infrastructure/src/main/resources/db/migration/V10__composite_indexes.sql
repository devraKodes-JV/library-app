-- ============================================================================
-- V10__composite_indexes.sql
-- Índices compuestos para mejorar performance en listados y filtros.
-- ============================================================================

-- Works: filtrado por categoría ordenado por fecha de creación
CREATE INDEX idx_works_category_created ON works (category_id, created_at);

-- Works: filtrado por idioma ordenado por fecha de creación
CREATE INDEX idx_works_language_created ON works (original_language_id, created_at);

-- Editions: filtrado por obra ordenado por número de edición
CREATE INDEX idx_editions_work_number ON editions (work_id, edition_number);
