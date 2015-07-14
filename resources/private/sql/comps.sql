-- name: create-comps-table!
-- Initializes a table for competitions
CREATE TABLE IF NOT EXISTS comps (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id INT UNSIGNED NOT NULL,
    comp_name VARCHAR(64),
    PRIMARY KEY(id),
    INDEX(user_id),

    FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON UPDATE CASCADE ON DELETE CASCADE
)
