(ns compositor.models.users
  (:require [yesql.core :refer [defqueries]]
            [compositor.models.db :refer [mysql-db]]))

(defqueries "private/sql/users.sql")

(defn get-user
  [username]
  (select-user mysql-db username))

(defn get-password
  "Gets the password hash for a given username"
  [username]
  (-> (select-password mysql-db username)
      (first) ; we know that usernames will be unique, take the first selected row
      (:password)))

(defn create-user
  "Registers a username and their hashed password
  Table requires usernames to be unique, transaction will fail if not
  Returns nil on failure"
  [username password]
  (try
    (insert-user! mysql-db username password)
    (catch java.sql.SQLException ex (prn (str "Caught exception: " (.getMessage ex)))))) ; we should have first class logging here
