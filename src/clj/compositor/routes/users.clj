(ns compositor.routes.users
  (:require [buddy.auth :refer [authenticated? throw-unauthorized]]
            [compojure.core :refer :all]
            [compositor.models.comps :as comps]
            [compositor.views.users :as layout]))

;; It'd be pretty sweet to rewrite competition management as a single-page app in Secretary
(defn home
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized)
    (let [username (get-in request [:session :identity])
          competitions (comps/get-competitions (name username))]
      (layout/home competitions))))

(defroutes user-routes
  (GET "/" [] home))
