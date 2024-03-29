(ns compositor.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.util.response :refer [redirect]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.backends.session :refer [session-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
            [compositor.routes.base :refer [base-routes]]))

(defn init []
  (println "compositor is starting"))

(defn destroy []
  (println "compositor is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found (redirect "/error-404")))

(defn unauthorized-handler
  "When user is not authorized, check if they're authenticated
  If so, they do not possess valid permissions, return 403
  Else ask them to login, and redirect to the attempted resource"
  [request metadata]
  (cond
    (authenticated? request) (-> (redirect "/error-403")
                                 (assoc :status 403))
    :else (let [current-url (:uri request)]
            (redirect (format "/login?next=%s" current-url)))))

(def auth-backend
  (session-backend {:unauthorized-handler unauthorized-handler}))

(defn logging [chain]
  (fn [request]
    (chain request)))

(def app
  (-> (routes base-routes app-routes)
      (wrap-authorization auth-backend)
      (wrap-authentication auth-backend)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (handler/site)
      (wrap-base-url)))
