-- name: create-users-table!
-- Initializes the table for users
CREATE TABLE IF NOT EXISTS users (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
    email VARCHAR(32) NOT NULL, 
    password BINARY(60) NOT NULL,
    PRIMARY KEY(id)
)
