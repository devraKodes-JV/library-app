-- ============================================================================
-- V6__books_seed.sql
-- Datos iniciales del módulo Books.
-- ============================================================================

-- Languages ----------------------------------------------------------------
INSERT INTO languages (code, name, enabled) VALUES ('es', 'Spanish', TRUE);
INSERT INTO languages (code, name, enabled) VALUES ('en', 'English', TRUE);
INSERT INTO languages (code, name, enabled) VALUES ('fr', 'French', TRUE);
INSERT INTO languages (code, name, enabled) VALUES ('de', 'German', TRUE);
INSERT INTO languages (code, name, enabled) VALUES ('pt', 'Portuguese', TRUE);

-- Book Formats -------------------------------------------------------------
INSERT INTO book_formats (code, name, description, enabled) VALUES ('HARDCOVER', 'Hardcover', 'Hardcover edition', TRUE);
INSERT INTO book_formats (code, name, description, enabled) VALUES ('PAPERBACK', 'Paperback', 'Paperback edition', TRUE);
INSERT INTO book_formats (code, name, description, enabled) VALUES ('EBOOK', 'E-book', 'Digital edition', TRUE);
INSERT INTO book_formats (code, name, description, enabled) VALUES ('AUDIOBOOK', 'Audiobook', 'Audio edition', TRUE);

-- Publishers --------------------------------------------------------------
INSERT INTO publishers (name, country, website, enabled) VALUES ('Penguin Random House', 'USA', 'https://www.penguinrandomhouse.com', TRUE);
INSERT INTO publishers (name, country, website, enabled) VALUES ('HarperCollins', 'USA', 'https://www.harpercollins.com', TRUE);
INSERT INTO publishers (name, country, website, enabled) VALUES ('Planeta', 'Spain', 'https://www.planeta.es', TRUE);

-- Authors ------------------------------------------------------------------
INSERT INTO authors (first_name, last_name, biography, birth_date, death_date, enabled) VALUES ('Gabriel', 'García Márquez', 'Colombian novelist', '1927-03-06', '2014-04-17', TRUE);
INSERT INTO authors (first_name, last_name, biography, birth_date, death_date, enabled) VALUES ('J.K.', 'Rowling', 'British author', '1965-07-31', NULL, TRUE);
INSERT INTO authors (first_name, last_name, biography, birth_date, death_date, enabled) VALUES ('George', 'Orwell', 'English novelist', '1903-06-25', '1950-01-21', TRUE);

-- Categories ---------------------------------------------------------------
INSERT INTO categories (code, name, description, enabled) VALUES ('FIC', 'Fiction', 'Fiction books', TRUE);
INSERT INTO categories (code, name, description, enabled) VALUES ('NON-FIC', 'Non-Fiction', 'Non-fiction books', TRUE);
INSERT INTO categories (code, name, description, enabled) VALUES ('SCI', 'Science', 'Science books', TRUE);
INSERT INTO categories (code, name, description, enabled) VALUES ('HIS', 'History', 'History books', TRUE);

-- Works --------------------------------------------------------------------
INSERT INTO works (title, subtitle, original_language_id, category_id, summary, enabled) VALUES ('One Hundred Years of Solitude', NULL, 1, 1, 'Multi-generational saga of the Buendía family.', TRUE);
INSERT INTO works (title, subtitle, original_language_id, category_id, summary, enabled) VALUES ('Harry Potter and the Philosopher''s Stone', NULL, 2, 1, 'A young wizard begins his journey.', TRUE);
INSERT INTO works (title, subtitle, original_language_id, category_id, summary, enabled) VALUES ('1984', NULL, 2, 1, 'Dystopian social science fiction.', TRUE);
