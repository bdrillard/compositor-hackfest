(ns compositor.routes.base
  (:require [ring.util.response :refer [redirect response]]
            [buddy.hashers :refer [check encrypt]]
            [compojure.core :refer :all]
            [compositor.views.base :as layout]
            [compositor.models.users :refer [get-password get-user create-user]]
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

(defn register
  [request]
  (layout/register))

(defn login
  [request]
  (layout/login))

(defn logout
  [request]
  (-> (redirect "/")
      (assoc :session {})))

(defn login-authenticate
  "Session-based auth, if our username exists, compare passwords
  If equal, add username to session as identity"
  [request]
  (let [username (get-in request [:form-params "username"])
        password (get-in request [:form-params "password"])
        session (:session request)
        found-password (get-password username)
        errors (cond
                 (not found-password) {:username "The given email is not registered"}
                 (not (check password found-password)) {:password "Incorrect password"}
                 :else nil)]
    (if (seq errors)
      (response (layout/login errors))
      (let [next-url (get-in request [:query-params "next"] "/")
            updated-session (assoc session :identity (keyword username))]
        (-> (redirect next-url)
            (assoc :session updated-session))))))

(defn register-user
  "Registers a new user by ensuring their username does not exist, and then
  hashing their password"
  [request]
  (let [username (get-in request [:form-params "username"])
        password (get-in request [:form-params "password"])
        confirm (get-in request [:form-params "confirm"])
        session (:session request)
        errors (cond-> {}
                 (not= password confirm) (assoc :password "Passwords did not match")
                 (seq (get-user username)) (assoc :username "Given email has already been registered"))]
    (if (seq errors)
      (response (layout/register errors))
      (let [encrypted-password (encrypt password)
            updated-session (assoc session :identity (keyword username))]
        (create-user username encrypted-password)
        (-> (redirect "/user")
            (assoc :session updated-session))))))

(defroutes base-routes
  (GET "/" [] (home))
  (GET "/error-404" [] error-404)
  (GET "/error-403" [] error-403)

  (context "/user" [] user/user-routes)

  (GET "/register" [] register)
  (POST "/register" [] register-user)
  (GET "/login" [] login)
  (POST "/login" [] login-authenticate)
  (GET "/logout" [] logout))
