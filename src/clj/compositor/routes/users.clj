(ns compositor.routes.users
  (:require [buddy.auth :refer [authenticated? throw-unauthorized]]
            [ring.util.response :refer [response]]
            [compojure.core :refer :all]
            [compositor.models.comps :as comps]
            [compositor.views.users :as layout]))

;; It'd be pretty sweet to rewrite competition management as a single-page app in Secretary
(defn home
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized)
    (let [username (-> request (get-in [:session :identity]) name)
          competitions (comps/get-competitions username)]
      (layout/home competitions))))

(defn new-compet
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized)
    (layout/new-compet)))

(defn create-compet
  [request]
  (let [username (-> request (get-in [:session :identity]) name)
        params (:body request)]
    (response {:body [(comps/create-compet username params)]})))

(defn new-graph
  [request]
  (if-not (authenticated? request)
    (throw-unauthorized)
    (let [id (get-in request [:route-params :id])]
      (layout/new-graph id request))))
  
(defroutes user-routes
  (GET "/" [] home)
  (GET "/compet" [] new-compet)
  (PUT "/compet" [] create-compet)
  (GET "/graph/:id" [] new-graph))
