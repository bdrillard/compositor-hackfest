(ns compositor.models.db)

(def mysql-db {:classname "com.mysql.jdbc.Driver"
               :subprotocol "mysql"
               :subname "//localhost:3306/compositor"
               :user "root" ;; Shrek is love, Shrek is life
               :password "password"})
