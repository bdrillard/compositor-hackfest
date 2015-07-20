(ns compositor.models.db
  (:require [clojure.java.jdbc :as jdbc]))

(def mysql-db {:classname "com.mysql.jdbc.Driver"
               :subprotocol "mysql"
               :subname "//localhost:3306/compositor"
               :user "root" ;; Shrek is love, Shrek is life
               :password "password"})

;; INSERT_LAST_ID() requires the connection of the previous insert query to be
;; maintained. Here we run an INSERT and SELECT on the same connection without
;; letting it close in between, else LAST_INSERT_ID() always returns 0
;; Goodness gracious this is a crazy gotcha
(defn insert-with-return-id!
  [db-spec table row]
  (let [db-conn (jdbc/get-connection db-spec)
        columns (clojure.string/join ", " (map name (keys row)))
        values (clojure.string/join ", " (vals row))
        insert-vector [(str "INSERT INTO " table " (" columns ") VALUES (" values ")")]
        query-vector ["SELECT LAST_INSERT_ID() AS id"]]
    (-> (jdbc/with-db-transaction [db-conn db-spec]
          (jdbc/execute! db-conn insert-vector)
          (jdbc/query db-conn query-vector))
        first
        :id)))
