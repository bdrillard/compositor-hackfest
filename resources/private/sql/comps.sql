-- name: create-comps-table!
-- Initializes a table for competitions
CREATE TABLE IF NOT EXISTS comps (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(36) NOT NULL,
    user_id INT UNSIGNED NOT NULL,
    comp_name VARCHAR(64),
    PRIMARY KEY(id),
    INDEX(user_id),

    FOREIGN KEY(user_id)
        REFERENCES users(id)
        ON UPDATE CASCADE ON DELETE CASCADE
)

-- name: select-comps
-- Gets a list of competitions and their fields registered to a user
SELECT comps.comp_name AS comp_name, comps.uuid AS uuid, num_fields.name AS field_name
    FROM users
        JOIN comps ON users.id = comps.user_id
        JOIN num_fields ON comps.id = num_fields.comp_id
    WHERE users.email = :username
UNION
SELECT comps.comp_name AS comp_name, comps.uuid AS uuid, bool_fields.name AS field_name
    FROM users
        JOIN comps ON users.id = comps.user_id
        JOIN bool_fields ON comps.id = bool_fields.comp_id
    WHERE users.email = :username
UNION
SELECT comps.comp_name AS comp_name, comps.uuid AS uuid, categ_fields.name AS field_name
    FROM users
        JOIN comps ON users.id = comps.user_id
        JOIN categ_fields ON comps.id = categ_fields.comp_id
    WHERE users.email = :username

-- name: select-comp-id
-- Gets the ID of the most recently created competition
SELECT LAST_INSERT_ID() AS id
