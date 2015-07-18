(ns compositor.models.comps
  (:require [yesql.core :refer [defqueries]]
            [compositor.models.db :refer [mysql-db]]))

(defqueries "private/sql/comps.sql")

(defn get-competitions
  "Gets a list of competitions, their uuids, and their fields"
  [username]
  (for [compet (->> (select-comps mysql-db username)
                    (group-by :comp_name))]
    [(first compet)
     (-> compet second first :uuid)
     (reduce #(conj %1 (:field_name %2)) [] (second compet))]))
