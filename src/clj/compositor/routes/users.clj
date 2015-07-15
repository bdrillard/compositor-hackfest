(ns compositor.routes.users
  (:require [buddy.auth :refer [authenticated? throw-unauthorized]]
            [compojure.core :refer :all]
            [compositor.views.users :as layout]))

(defn home
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized)
    (layout/home)))

(defroutes user-routes
  (GET "/" request (home request)))
