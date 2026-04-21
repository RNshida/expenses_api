-- ふるさと納税テーブル追加
-- PostgreSQL で手動実行してください: psql -d expenses_db -f add_furusato_tables.sql

CREATE TABLE IF NOT EXISTS furusato_contributions (
    id           SERIAL PRIMARY KEY,
    user_id      INTEGER        NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    fiscal_year  INTEGER        NOT NULL,
    municipality VARCHAR(50)    NOT NULL,
    product_name VARCHAR(20),
    amount       NUMERIC(15, 2) NOT NULL,
    status       VARCHAR(30)    NOT NULL DEFAULT '未申請',
    created_at   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS furusato_configs (
    id            SERIAL PRIMARY KEY,
    user_id       INTEGER        NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    fiscal_year   INTEGER        NOT NULL,
    annual_income INTEGER,
    limit_amount  NUMERIC(15, 2),
    CONSTRAINT uq_furusato_config UNIQUE (user_id, fiscal_year)
);
