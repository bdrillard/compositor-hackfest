(ns compositor.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]))

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

(def nav-login
  [:form.navbar-form.navbar-right {:role "login"}
   [:div.form-group
    [:input.form-control {:type "text" :placeholder "Email"}]
    [:input.form-control {:type "password" :placeholder "Password"}]]
   [:button.btn.btn-default {:type "submit"} "Login"]])

(defn home
  [& body]
  (html5
    (page-head)
    [:body
     (page-nav nil [nav-login])
     [:div.container.fluid
      [:div.col-md-8.col-md-offset-2.col-sm-10.col-sm-offset-1
       [:div.jumbotron
        [:div.row
         [:h1 "Compositor"]]
        [:div.row
         [:p "Compositor let's you build, score, and administer custom competitions!"]
         [:p "Register to get started"]]
        [:div.row
         [:a {:href "#"}
          [:span.btn.btn-primary "Register"]]]]]]]))


(defn new-comp [& body]
  (html5
    (page-head)
    [:body
     (page-nav nil nil)
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
