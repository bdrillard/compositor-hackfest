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

-- name: insert-categ-field!
-- Creates a new category field
INSERT INTO categ_fields (id, comp_id, name, comments)
    VALUES (NULL, :comp_id, :name, :comments)

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

-- name: insert-categ!
-- Creates a category-value that a category field can take on
INSERT INTO categs (id, categ_field_id, category)
    VALUES (NULL, :categ_field_id, :category)

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

-- name: insert-num-field!
-- Creates a new number field
INSERT INTO num_fields (id, comp_id, name, num_type, upper_bound, lower_bound, negative, comments)
    VALUES (NULL, :comp_id, :name, :num_type, :upper_bound, :lower_bound, :negative, :comments)

-- name: create-bool-fields-table!
-- Initializes a table for competition boolean fields
CREATE TABLE IF NOT EXISTS bool_fields (
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

-- name: insert-bool-field!
-- Creates a new boolean field
INSERT INTO bool_fields (id, comp_id, name, comments)
    VALUES (NULL, :comp_id, :name, :comments)
