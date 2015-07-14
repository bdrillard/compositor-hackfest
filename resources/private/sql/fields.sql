-- name: create-categ-fields-table!
-- Initializes a table for competition category fields
CREATE TABLE IF NOT EXISTS categ_fields (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    comp_id INT UNSIGNED NOT NULL,
    name VARCHAR(32) NOT NULL,
    comments VARCHAR(512),

    PRIMARY KEY(id),
    INDEX(comp_id),

    FOREIGN KEY(comp_id)
        REFERENCES comps(id)
        ON UPDATE CASCADE ON DELETE CASCADE
)

-- name: create-categs-table!
-- Initializes a table for individual categories of a category field
CREATE TABLE IF NOT EXISTS categs (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    categ_field_id INT UNSIGNED NOT NULL,
    category VARCHAR(32),

    PRIMARY KEY(id),
    INDEX(categ_field_id),

    FOREIGN KEY(categ_field_id)
        REFERENCES categ_fields(id)
        ON UPDATE CASCADE ON DELETE CASCADE
)

-- name: create-num-fields-table!
-- Initializes a table for competition number fields
CREATE TABLE IF NOT EXISTS num_fields (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    comp_id INT UNSIGNED NOT NULL,
    name VARCHAR(32) NOT NULL,
    num_type ENUM("integer", "double") NOT NULL, -- database ENUMs are the plague
    upper_bound DOUBLE,
    lower_bound DOUBLE,
    negative TINYINT(1) UNSIGNED,
    comments VARCHAR(512),

    PRIMARY KEY(id),
    INDEX(comp_id),
    
    FOREIGN KEY(comp_id)
        REFERENCES comps(id)
        ON UPDATE CASCADE ON DELETE CASCADE
)

-- name: create-bool-fields-table!
-- Initializes a table for competition boolean fields
CREATE TABLE IF NOT EXISTS bool_fields (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    comp_id INT UNSIGNED NOT NULL,
    name VARCHAR(32) NOT NULL,
    commends VARCHAR(512),

    PRIMARY KEY(id),
    INDEX(comp_id),

    FOREIGN KEY(comp_id)
        REFERENCES comps(id)
        ON UPDATE CASCADE ON DELETE CASCADE
)
