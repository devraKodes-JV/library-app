-- ============================================================================
-- V9__unique_constraints.sql
-- Unique constraints adicionales para evitar duplicados.
-- ============================================================================

-- Work Authors: un autor no puede estar duplicado en la misma obra
ALTER TABLE work_authors ADD CONSTRAINT uq_work_authors_work_author UNIQUE (work_id, author_id);

-- Edition Authors: un autor no puede estar duplicado en la misma edición
ALTER TABLE edition_authors ADD CONSTRAINT uq_edition_authors_edition_author UNIQUE (edition_id, author_id);

-- Editions: no puede haber dos ediciones con el mismo número para la misma obra
ALTER TABLE editions ADD CONSTRAINT uq_editions_work_number UNIQUE (work_id, edition_number);
