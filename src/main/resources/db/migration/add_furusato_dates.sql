-- ふるさと納税：納税日・申請日カラム追加
-- PostgreSQL で手動実行してください: psql -d expenses_db -f add_furusato_dates.sql

ALTER TABLE furusato_contributions
    ADD COLUMN IF NOT EXISTS donation_date    DATE,
    ADD COLUMN IF NOT EXISTS application_date DATE;
