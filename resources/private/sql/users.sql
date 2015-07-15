-- name: create-users-table!
-- Initializes the table for users
CREATE TABLE IF NOT EXISTS users (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
    email VARCHAR(32) NOT NULL UNIQUE, 
    password BINARY(60) NOT NULL,
    PRIMARY KEY(id)
)

-- name: select-password
-- Gets password for a given username
SELECT password FROM users 
    WHERE email = :username
