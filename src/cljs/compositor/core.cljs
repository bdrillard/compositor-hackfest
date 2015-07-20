;;;; Main Public Reagent component namespace
(ns compositor.core
  (:require [reagent.core :as reagent :refer [render create-class render-component]]
            [compositor.fields :as fields]
            [compositor.graph :as graph]))

(defn ^:export fields
  []
  (render [fields/fields-declaration-app]
          (.getElementById js/document "newComp")))

(defn ^:export editor
  []
  (render [graph/graph-app]
          (.getElementById js/document "editor")))
