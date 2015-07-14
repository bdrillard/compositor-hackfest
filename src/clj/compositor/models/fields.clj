(ns compositor.models.fields
  (:require [yesql.core :refer [defqueries]]
            [compositor.models.db :refer [mysql-db]]))

(defqueries "private/sql/fields.sql")
