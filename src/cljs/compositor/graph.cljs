(ns compositor.graph
  (:require [reagent.core :as reagent :refer [atom]]))

;; hardcoded graph for demo, render logic is genuine though
(defonce graph (atom {:1 {:id 1
                          :metadata {:pos [100 100]
                                     :type :in
                                     :data "rubric_1"}
                          :neighbors [4]}
                      :2 {:id 2
                          :metadata {:pos [100 150]
                                     :type :in
                                     :data "rubric_2"}
                          :neighbors [4]}
                      :3 {:id 3
                          :metadata {:pos [100 200]
                                     :type :in
                                     :data "rubric_3"}
                          :neighbors [4]}
                      :4 {:id 4
                          :metadata {:pos [500 150]
                                     :type :agg
                                     :func :plus}
                          :neighbors [5]}
                      :5 {:id 5
                          :metadata {:pos [700 150]
                                     :type :out
                                     :data "score"}}

                      :6 {:id 6
                          :metadata {:pos [100 500]
                                     :type :in
                                     :data "weight"}
                          :neighbors [8]}
                      :7 {:id 7
                          :metadata {:pos [100 550]
                                     :type :in
                                     :data "length"}
                          :neighbors [9]}
                      :8 {:id 8
                          :metadata {:pos [300 400]
                                     :type :logical
                                     :func :gt
                                     :threshold 5}
                          :neighbors [10]}
                      :9 {:id 9
                          :metadata {:pos [300 650]
                                     :type :logical
                                     :func :gte
                                     :threshold 9}
                          :neighbors [10]}
                      :10 {:id 10
                           :metadata {:pos [500 500]
                                      :type :agg
                                      :func :or}
                           :neighbors [11]}
                      :11 {:id 11
                           :metadata {:pos [700 500]
                                      :type :out
                                      :data "disqualified"}}}))

(defonce types (atom ["rubric_1" "rubric_2" "rubric_3" "score" "weight" "length" "disqualified"]))

(defn render-select
  [id value]
  [:div.form-group
   (apply conj [:select.form-control {:value value}]
          (for [t @types]
            [:option {:value t} t]))])

(defn render-logical
  [sym threshold]
  [:div.form-group {:style {:width 100}}
   [:div.input-group
    [:div.input-group-addon (cond (= sym :lt) "<"
                                  (= sym :lte) "≤"
                                  (= sym :gt) ">"
                                  (= sym :gte) "≥"
                                  (= sym :e) "="
                                  (= sym :ne) "≠")]
    [:input.form-control {:type "number" :value threshold}]]])

(defn render-agg
  [sym]
  [:h1 {:style {:margin-left 10 :margin-right 10}}
   (cond (= sym :or) "OR"
             (= sym :and) "AND"
             (= sym :plus) "+"
             (= sym :avg) "AVERAGE")])

(defn render-node
  [node]
  (.log js/console (str node))
  (let [id (:id node)
        node-type (get-in node [:metadata :type])
        [x y] (get-in node [:metadata :pos])
        neighbors (get-in node [:neighbors])
        context (.getContext (.getElementById js/document "cvs") "2d")]
    (do
      (doseq [neighbor neighbors]
        (let [[nx ny] (get-in @graph [(keyword (str neighbor)) :metadata :pos])]
          (doto context
            (.beginPath)
            (.moveTo x (- y 50))
            (.lineTo nx (- ny 50))
            (.stroke))))
      (cond (= node-type :in)
            [:div.block {:style {:left x :top y :background "#0000FF"}}
             (render-select id (get-in node [:metadata :data]))]
            (= node-type :out)
            [:div.block {:style {:left x :top y :background "#FF0000"}}
             (render-select id (get-in node [:metadata :data]))]
            (= node-type :logical)
            [:div.block {:style {:left x :top y :background "#FFA500"}}
             (render-logical (get-in node [:metadata :func])
                             (get-in node [:metadata :threshold]))]
            (= node-type :agg)
            [:div.block {:style {:left x :top y :background "#800080" :color "#FFFFFF"}}
             (render-agg (get-in node [:metadata :func]))]))))

(defn graph-app
  [props]
  (let [graph-map (vals @graph)]
    [:div 
     (for [node graph-map]
       ^{:key (:id node)} [render-node node])]))
