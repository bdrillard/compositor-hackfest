(ns compositor.views.users
  (:require [hiccup.page :refer [html5]]))

(defn home
  []
  (html5
    [:p "Hello, world!"]))

(defn new-fields
  [& body]
  (html5
    (page-head)
    [:body
     (page-nav nil [nav-logout])
     [:div.container-fluid
      [:div.col-md-8.col-md-offset-2.col-sm-10.col-sm-offset-1
       [:div.panel.panel-primary
        [:div.panel-heading
         [:h3.panel-title "Define the judging fields of a new competition"]]
        [:div#newComp.panel-body]] ;; Field forms get attached here
       [:div.panel.panel-info
        [:div.panel-heading
         [:h3.panel-title "Help"]]
        [:div.panel-body "Create new fields for data to be entered for your competition"]]]]
     [:script {:type "text/javascript"} "compositor.core.run()"]]))
