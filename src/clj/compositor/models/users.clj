(ns compositor.models.users
  (:require [yesql.core :refer [defqueries]]
            [compositor.models.db :refer [mysql-db]]))

(defqueries "private/sql/users.sql")
