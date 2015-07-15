(ns compositor.models.users
  (:require [yesql.core :refer [defqueries]]
            [compositor.models.db :refer [mysql-db]]))

(defqueries "private/sql/users.sql")

(defn get-password
  "Gets the password hash for a given username"
  [username]
  (->> username
       (select-password mysql-db)
       (first) ; we know that usernames will be unique, take the first matched row
       (:password)))
