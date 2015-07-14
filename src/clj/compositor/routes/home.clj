(ns compositor.routes.home
  (:require [compojure.core :refer :all]
            [compositor.views.layout :as layout]))

(defn home []
  (layout/home))

(defroutes home-routes
  (GET "/" [] (home)))
