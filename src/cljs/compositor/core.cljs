(ns compositor.core
  (:require [reagent.core :as reagent :refer [render]]
            [compositor.fields :as fields]))

(defn ^:export run 
  []
  (render [fields/comp-app]
          (.getElementById js/document "newComp")))
