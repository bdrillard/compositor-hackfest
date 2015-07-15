(ns compositor.routes.base
  (:require [ring.util.response :refer [redirect response]]
            [buddy.hashers :refer [check]]
            [compojure.core :refer :all]
            [compositor.views.base :as layout]
            [compositor.models.users :refer [get-password]]
            [compositor.routes.users :as user]))

(defn home
  []
  (layout/home))

(defn error-404
  [request]
  (layout/page-404))

(defn error-403
  [request]
  (layout/page-403))

(defn login
  [request]
  (layout/login))

(defn logout
  [request]
  (-> (redirect "/login")
      (assoc :session {})))

(defn login-authenticate
  "Session-based auth, if our username exists, compare passwords
  If equal, add username to session as identity"
  [request]
  (let [username (get-in request [:form-params "username"])
        password (get-in request [:form-params "password"])
        session (:session request)
        found-password (get-password username)]
    (if (and found-password (check password found-password))
      (let [next-url (get-in request [:query-params "next"] "/")
            updated-session (assoc session :identity (keyword username))]
        (-> (redirect next-url)
            (assoc :session updated-session)))
      (redirect "/login"))))

(defroutes base-routes
  (GET "/" [] (home))
  (GET "/error-404" [] error-404)
  (GET "/error-403" [] error-403)

  (context "/user" [] user/user-routes)

  (GET "/login" [] login)
  (POST "/login" [] login-authenticate)
  (GET "/logout" [] logout))
