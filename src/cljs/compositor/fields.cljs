;;;; Builds Reagent interface for competition field declaration
(ns compositor.fields
  (:require [reagent.core :as reagent :refer [atom]]
            [ajax.core :refer [POST]]))

(defonce comp-name (atom nil))
(defonce fields (atom (sorted-map)))
(defonce counter (atom 0))

(defn name-input
  "Returns an element that edits the name of the competition"
  []
  [:input.form-control {:type "text" 
                        :placeholder "Our Rad Competition Name"
                        :value @comp-name
                        :on-change #(reset! comp-name (-> %
                                                          .-target
                                                          .-value))}])

(defn add-field
  "A new competition field, initalizes values to nil"
  []
  (let [id (swap! counter inc)]
    (swap! fields assoc id {:id id
                            :fname nil
                            :ftype nil
                            :enums nil
                            :comms nil
                            :lower-bound nil
                            :upper-bound nil
                            :negative? nil})))

(defn delete-field
  "Removes a field from our list field-list by id"
  [id]
  (swap! fields dissoc id))

(defn field-name
  "Returns an element containing the name of a field"
  [id fname]
  [:div.form-group
   [:input.form-control {:type "text"
                         :placeholder "Field name"
                         :value fname
                         :on-change #(swap! fields assoc-in [id :fname] (-> %
                                                                            .-target
                                                                            .-value))}]])

(defn field-type
  "Returns an element specifying the type of a field"
  [id ftype]
  [:div.form-group
   [:select.form-control {:value ftype
                          :on-change #(swap! fields assoc-in [id :ftype] (-> %
                                                                             .-target
                                                                             .-value))}
    [:option {:value "" :disabled true} "Field type"]
    [:option {:value "enum"} "Categories"]
    [:option {:value "integer"} "Whole Number"]
    [:option {:value "double"} "Decimal Number"]
    [:option {:value "boolean"} "Yes/No"]]])

(defn number-attrs
  "Returns elements specifying attributes of a number field-type"
  [id ftype lower-bound upper-bound negative?]
  (when (or (= "integer" ftype) (= "double" ftype))
    [:div
     [:div.form-group
      [:input.form-control {:type "number"
                            :placeholder "Lower bound"
                            :value lower-bound
                            :on-change #(swap! fields assoc-in [id :lower-bound] (-> %
                                                                                     .-target
                                                                                     .-value))}]]
     [:div.form-group
      [:input.form-control {:type "number"
                            :placeholder "Upper bound"
                            :value upper-bound
                            :on-change #(swap! fields assoc-in [id :upper-bound] (-> %
                                                                                     .-target
                                                                                     .-value))}]]
     [:div.checkbox
      [:input {:type "checkbox"
               :checked negative?
               :on-change #(swap! fields assoc-in [id :negative?] (-> %
                                                                      .-target
                                                                      .-checked))}
       "Can be negative?"]]]))

(defn enum-attrs
  "Returns an element containing a raw string of enums"
  [id ftype enums]
  (when (= "enum" ftype)
    [:div.form-group
     [:input.form-control {:type "text"
                           :placeholder "Comma, Separated, Categories"
                           :value enums
                           :on-change #(swap! fields assoc-in [id :enums] (-> %
                                                                              .-target
                                                                              .-value))}]]))

(defn field-comms
  "Returns an element specifying field comments"
  [id comms]
  [:textarea.form-control {:rows 3 
                           :placeholder "Comments for our field, leave scoring notes for judges."
                           :value comms
                           :on-change #(swap! fields assoc-in [id :comms] (-> %
                                                                             .-target
                                                                             .-value))}])

(defn field-item
  "Returns an element specifying all aspects of a field" 
  [{:keys [id fname ftype enums comms lower-bound upper-bound negative?]}]
  [:div.row
   [:div.row
    [:a.col-xs-1.pull-right {:href "#"}
     [:span.glyphicon.glyphicon-remove.danger {:on-click #(delete-field id)}]]]
   [:div.row
    [:div.form-group
     [:label.col-xs-2.control-label "Description"]
     [:div.col-xs-9.collapse.in
      [:div.form-inline 
       (field-name id fname)
       (field-type id ftype)
       (number-attrs id ftype lower-bound upper-bound negative?)
       (enum-attrs id ftype enums)]]]]
   [:div.row
    [:div.form-group
     [:label.col-xs-2.control-label "Comments"]
     [:div.col-xs-9
      (field-comms id comms)]]]])


(defn submit-fields
  "Submits a POST request with a JSON encoding of the fields"
  [fields]
  (POST "/api/new-comp"
        {:params {:body (vals fields)}
         :response-format :json}))

(defonce init (add-field)) ;; an initial field

(defn fields-declaration-app
  "Competition field declaration component"
  [props]
  (let [comp-name @comp-name
        fields (vals @fields)]
    [:form.form-horizontal
     [:div.row
      [:div.form-group
       [:label.col-xs-2.control-label "Name"]
       [:div.col-xs-9
        (name-input)]]]
     [:hr]
     [:div.row
      [:a.col-xs-2 {:href "#"}
       [:span.btn.btn-success {:on-click #(add-field)} "Add Field"]]]
     (when (-> fields count pos?)
       (for [field fields]
         ^{:key (:id field)} [field-item field]))
     [:hr]
     [:button.btn.btn-default.pull-right {:type "submit"} "Submit"]]))
