;;;; Main Public Reagent component namespace
(ns compositor.core
  (:require [reagent.core :as reagent :refer [render]]
            [compositor.fields :as fields]))

(defn ^:export fields
  []
  (render [fields/fields-declaration-app]
          (.getElementById js/document "newComp")))
