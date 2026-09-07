-- Expand CODE columns to accommodate auto-generated UUIDs with prefixes (e.g., CAT-a1b2c3D4)
ALTER TABLE categories  ALTER COLUMN code VARCHAR(20);
ALTER TABLE languages   ALTER COLUMN code VARCHAR(20);
ALTER TABLE categories_AUD ALTER COLUMN code VARCHAR(20);
ALTER TABLE languages_AUD  ALTER COLUMN code VARCHAR(20);
