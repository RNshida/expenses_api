-- PostgreSQL initialization script
-- Run this manually before starting the application

CREATE DATABASE expenses_db;

\c expenses_db;

CREATE TABLE IF NOT EXISTS categories (
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL UNIQUE,
    display_order INTEGER DEFAULT 0,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS monthly_balances (
    id          SERIAL PRIMARY KEY,
    category_id INTEGER     NOT NULL REFERENCES categories (id) ON DELETE CASCADE,
    year_month  DATE        NOT NULL,
    amount      NUMERIC(15, 2) NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_category_month UNIQUE (category_id, year_month)
);

-- Sample categories
INSERT INTO categories (name, display_order) VALUES
    ('普通預金', 0),
    ('定期預金', 1),
    ('現金',     2)
ON CONFLICT (name) DO NOTHING;
