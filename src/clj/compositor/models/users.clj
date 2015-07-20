(ns compositor.models.users
  (:require [yesql.core :refer [defqueries]]
            [compositor.models.db :refer [mysql-db]]))

(defqueries "private/sql/users.sql")

(defn get-user
  [username]
  (first (select-user mysql-db username)))

(defn create-user
  "Registers a username and their hashed password
  Table requires usernames to be unique, transaction will fail if not
  Returns nil on failure"
  [username password]
  (try
    (insert-user! mysql-db username password)
    (catch java.sql.SQLException ex (prn (str "Caught exception: " (.getMessage ex)))))) ; we should have first class logging here
