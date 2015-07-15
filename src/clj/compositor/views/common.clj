(ns compositor.views.common
  (:require [hiccup.page :refer [include-css include-js]]))

(defn page-head
  [& body]
  [:head
   [:title "Compositor - New Competition"]
   (include-css "/css/bootstrap.min.css")
   (include-css "/css/compositor.css")
   (include-js "/js/jquery-1.11.3.min.js")
   (include-js "/js/bootstrap.min.js")
   (include-js "/js/app.js")])

(defn page-nav
  [left right]
  [:nav.navbar.navbar-default
   [:div.container-fluid
    [:div.navbar-header
     [:a.navbar-brand {:href "/"} "Compositor"]]
    [:div.navbar-collapse
     [:ul.nav.navbar-nav
      (for [elem left]
        [:li elem])]
     [:ul.nav.navbar-nav.navbar-right
      (for [elem right]
        [:li elem])]]]])
