-- 種別テーブル
CREATE TABLE IF NOT EXISTS category_types (
    id            SERIAL PRIMARY KEY,
    user_id       INTEGER      NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    name          VARCHAR(100) NOT NULL,
    display_order INTEGER      NOT NULL DEFAULT 0,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_category_type_user_name UNIQUE (user_id, name)
);

-- カテゴリに種別FK追加
ALTER TABLE categories ADD COLUMN IF NOT EXISTS category_type_id INTEGER REFERENCES category_types(id) ON DELETE SET NULL;
