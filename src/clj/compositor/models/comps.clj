(ns compositor.models.comps
  (:require [yesql.core :refer [defqueries defquery]]
            [clojure.java.jdbc :as jdbc]
            [java-jdbc.ddl :as ddl]
            [clj-uuid :as uuid]
            [compositor.models.db :refer [mysql-db insert-with-return-id!]]
            [compositor.models.users :as users]
            [compositor.models.fields :as fields]))

(defqueries "private/sql/comps.sql")

(defn get-competitions
  "Gets a list of competitions, their uuids, and their fields"
  [username]
  (for [compet (->> (select-comps mysql-db username)
                    (group-by :comp_name))]
    (vector (first compet)
            (-> compet second first :uuid)
            (reduce #(conj %1 (:field_name %2)) [] (second compet)))))

;;; Table and fields creation
;;; Below there be dragons
(defn create-integer-fields
  [id fields]
  (prn fields)
  (for [field fields]
    (let [{:keys [fname negative? lower-bound upper-bound comms]} field
          lower-bound (when lower-bound (int (read-string lower-bound)))
          upper-bound (when upper-bound (int (read-string upper-bound)))]
      (fields/insert-num-field! mysql-db id fname "integer" upper-bound lower-bound negative? comms))))

(defn create-double-fields
  [id fields]
  (prn fields)
  (for [field fields]
    (let [{:keys [fname negative? lower-bound upper-bound comms]} field
          lower-bound (when lower-bound (double (read-string lower-bound)))
          upper-bound (when upper-bound (double (read-string upper-bound)))]
      (fields/insert-num-field! mysql-db id fname "double" upper-bound lower-bound negative? comms))))

(defn create-boolean-fields
  [id fields]
  (prn fields)
  (for [field fields]
    (let [{:keys [fname comms]} field]
      (fields/insert-bool-field! mysql-db id fname comms))))

(defn create-categ-fields
  [id fields]
  (prn fields)
  (for [field fields]
    (let [{:keys [fname enums comms]} field
          categ-field-id (insert-with-return-id! mysql-db
                                                 "categ_fields"
                                                 {:id "NULL"
                                                  :comp_id id
                                                  :name (str "\"" fname "\"")
                                                  :comments (str "\"" comms "\"")})]
      (prn categ-field-id)
      (for [categ (clojure.string/split enums #", ")]
        (fields/insert-categ! mysql-db categ-field-id categ)))))

(defn create-fields
  [id fields]
  (prn fields)
  (let [field-groups (group-by :ftype fields)
        integer-fields (get field-groups "integer")
        double-fields (get field-groups "double")
        boolean-fields (get field-groups "boolean")
        enum-fields (get field-groups "enum")]
    [(create-integer-fields id integer-fields)
     (create-double-fields id double-fields)
     (create-boolean-fields id boolean-fields)
     (create-categ-fields id enum-fields)]))

(defn enum->string
  [enums]
  (clojure.string/join ", "
                       (for [enum (clojure.string/split enums #", ")]
                         (str "\"" enum "\""))))

(defn sql-field
  [field]
  (let [{:keys [fname ftype enums negative?]} field]
    (cond (= ftype "boolean") [(keyword fname) "TINYINT(1)" "UNSIGNED"]
          (= ftype "integer") (if negative?
                                [(keyword fname) "INT"]
                                [(keyword fname) "INT" "UNSIGNED"])
          (= ftype "double") (if negative?
                               [(keyword fname) "DOUBLE"]
                               [(keyword fname) "DOUBLE" "UNSIGNED"])
          (= ftype "enum") [(keyword fname) (str "ENUM(" (enum->string enums) ")")])))

;; Clojure meta-programming on ddl/create-table
(defn compose-table
  [table-name table-fields]
  (eval `(ddl/create-table ~(str "`" table-name "`") ~@(map sql-field table-fields))))

(defn create-comp-with-id
  [uuid user-id compet-name]
  (insert-with-return-id! mysql-db 
                          "comps" 
                          {:id "NULL"
                           :uuid (str "\"" uuid "\"")
                           :user_id user-id
                           :comp_name (str "\"" compet-name "\"")}))

(defn create-compet
  [username params]
  (let [uuid (uuid/to-string (uuid/v1))
        user-id (-> username (users/get-user) :id)
        compet-name (:name params)
        compet-fields (:fields params)]
    (do 
      (prn compet-fields)
      (jdbc/db-do-commands mysql-db (compose-table uuid compet-fields))
      (let [comp-id (create-comp-with-id uuid user-id compet-name)]
        (prn comp-id)
        (create-fields comp-id compet-fields)))))
