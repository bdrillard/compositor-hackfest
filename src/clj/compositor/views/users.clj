(ns compositor.views.users
  (:require [hiccup.page :refer [html5]]
            [compositor.views.common :as common]))

(def nav-logout
  [[:a {:href "/logout"}
   [:span.btn.btn-default "Logout"]]])

(def nav-comps
  [[:a {:href "/user"} "Overview"]
   [:a {:href "/user/comps"} "Competitions"]])

(defn home
  [comps]
  (html5
    (common/page-head)
    [:body
     (common/page-nav nav-comps nav-logout)
     [:div.container-fluid
      [:div.col-md-8.col-md-offset-2.col-sm-10.col-sm-offset-1
       [:div.panel.panel-default
        [:div.panel-heading
         [:h3.panel-title "Competitions"]]
        [:div.panel-body
         [:div.btn-toolbar {:style "margin-bottom: 5px;"}
          [:a.btn.btn-success.pull-right {:href "/user/compet"}
           [:span.glyphicon.glyphicon-plus]
           " New Competition"]]
         (if (seq comps)
           [:table.table
            [:tr
             [:th "Name"]
             [:th "Identifier"]
             [:th "Fields"]]
            (for [compet comps]
              (let [comp-name (nth compet 0)
                    comp-uuid (nth compet 1)
                    comp-fields (nth compet 2)]
                [:tr
                 [:td [:a {:href (str "/user/compet" comp-uuid)} comp-name]]
                 [:td comp-uuid]
                 [:td (clojure.string/join ", " comp-fields)]]))]
           [:p "Bummer, you haven't made any competitions. We can fix that though!"])]]]]]))

(defn new-compet
  [& body]
  (html5
    (common/page-head)
    [:body
     (common/page-nav nil nav-logout)
     [:div.container-fluid
      [:div.col-md-8.col-md-offset-2.col-sm-10.col-sm-offset-1
       [:div.panel.panel-primary
        [:div.panel-heading
         [:h3.panel-title "Define the judging fields of a new competition"]]
        [:div#newComp.panel-body]] ; Field forms get attached here
       [:div.panel.panel-info
        [:div.panel-heading
         [:h3.panel-title "Help"]]
        [:div.panel-body "Create new fields for data to be entered for your competition"]]]]
     [:script {:type "text/javascript"} "compositor.core.fields()"]]))
