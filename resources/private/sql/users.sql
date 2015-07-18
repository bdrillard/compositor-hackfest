-- name: create-users-table!
-- Initializes the table for users
CREATE TABLE IF NOT EXISTS users (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT, 
    email VARCHAR(32) NOT NULL, 
    password BINARY(60) NOT NULL,
    PRIMARY KEY(id)
    CONSTRAINT UNIQUE(email)
)

-- name: select-user
-- Gets a user from a given username
SELECT * FROM users
    WHERE email = :username

-- name: select-password
-- Gets password for a given username
SELECT password FROM users 
    WHERE email = :username

-- name: insert-user!
-- Inerts a user into the users table
INSERT INTO users (id, email, password)
    VALUES (NULL, :username, :password)
