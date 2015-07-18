(ns compositor.views.base
  (:require [hiccup.page :refer [html5]]
            [compositor.views.common :as common]))

(def nav-login
  [:a {:href "/user"}
   [:span.btn.btn-default "Login"]])

(defn home
  [& body]
  (html5
    (common/page-head)
    [:body
     (common/page-nav nil [nav-login])
     [:div.container.fluid
      [:div.col-md-8.col-md-offset-2.col-sm-10.col-sm-offset-1
       [:div.jumbotron
        [:div.row
         [:h1 "Compositor"]]
        [:div.row
         [:p "Compositor let's you build, score, and administer custom competitions!"]
         [:p "Register to get started"]]
        [:div.row
         [:a {:href "/register"}
          [:span.btn.btn-primary "Register"]]]]]]]))

(defn register
  ([]
   (register {}))
  ([errors]
   (html5
     (common/page-head)
     [:body
      (common/page-nav nil [nav-login])
      [:div.container.fluid
       [:div.col-md-8.col-md-offset-2.col-sm-10.col-sm-offset-1
        [:div.jumbotron
         [:p "Register"]
         [:form {:method "post" :role "register"}
          [:div.row
           [:div.form-group
            [:label "Email address"]
            [:input.form-control {:type "text" :placeholder "Email" :name "username"}]]
           (when-let [message (:username errors)]
             [:div.alert.alert-danger message])]
          [:div.row
           [:div.form-group
            [:label "Password"]
            [:input.form-control {:type "password" :placeholder "Password" :name "password"}]]
           (when-let [message (:password errors)]
             [:div.alert.alert-danger message])]
          [:div.row
           [:div.form-group
            [:label "Confirm password"]
            [:input.form-control {:type "password" :placeholder "Confirm" :name "confirm"}]]]
          [:div.row
           [:div.form-group
            [:input.btn.btn-default {:type "submit"}]]]]]]]])))

(defn login
  ([]
   (login {}))
  ([errors]
   (html5
     (common/page-head)
     [:body
      (common/page-nav nil nil)
      [:div.container.fluid
       [:div.col-md-8.col-md-offset-2.col-sm-10.col-sm-offset-1
        [:div.jumbotron
         [:p "Login"]
         [:form {:method "post" :role "login"}
          [:div.row
           [:div.form-group
            [:label "Email address"]
            [:input.form-control {:type "text" :placeholder "Email" :name "username"}]]
           (when-let [message (:username errors)]
             [:div.alert.alert-danger message])]
          [:div.row
           [:div.form-group
            [:label "Password"]
            [:input.form-control {:type "password" :placeholder "Password" :name "password"}]]
           (when-let [message (:password errors)]
             [:div.alert.alert-danger message])]
          [:div.row
           [:div.form-group
            [:input.btn.btn-default {:type "submit"}]]]]]]]])))

(defn page-404
  []
  (html5
    (common/page-head)
    [:body
     (common/page-nav nil [nav-login])
     [:h1 "404"]]))

(defn page-403
  []
  (html5
    (common/page-head)
    [:body
     (common/page-nav nil [nav-login])
     [:h1 "403"]]))
