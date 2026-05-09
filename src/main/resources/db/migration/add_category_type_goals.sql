CREATE TABLE IF NOT EXISTS category_type_goals (
    id               SERIAL PRIMARY KEY,
    user_id          INTEGER      NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    category_type_id INTEGER      NOT NULL REFERENCES category_types(id) ON DELETE CASCADE,
    target_amount    NUMERIC(15,2),
    CONSTRAINT uq_category_type_goal UNIQUE (user_id, category_type_id)
);
