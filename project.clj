(defproject compositor "0.1.0-SNAPSHOT"
  :description "An application for representing and compiling arbitrary
               competition logic"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 ;; Clojure
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [ring/ring-json "0.3.1"]
                 [buddy/buddy-auth "0.6.0"]
                 [buddy/buddy-hashers "0.6.0"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [java-jdbc/dsl "0.1.3"]
                 [mysql/mysql-connector-java "5.1.36"]
                 [yesql "0.4.2"]
                 [danlentz/clj-uuid "0.1.6"]
                 ;; ClojureScript
                 [org.clojure/clojurescript "0.0-3211"]
                 [reagent "0.5.0"]
                 [reagent-forms "0.5.4"]
                 [secretary "1.2.3"]
                 [cljs-ajax "0.3.13"]]
  :plugins [[lein-ring "0.8.12"]
            [lein-cljsbuild "1.0.6"]
            [lein-figwheel "0.3.3"]]
  :source-paths ["src/clj"]
  :ring {:handler compositor.handler/app
         :init compositor.handler/init
         :destroy compositor.handler/destroy}
  :profiles {:uberjar {:aot :all}
             :production {:ring {:open-browser? false
                                 :stacktraces? false
                                 :auto-reload? false}}
             :dev {:dependencies [[ring-mock "0.1.5"] 
                                  [ring/ring-devel "1.3.1"]]}}
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src/cljs"]
                        :figwheel {:on-jsload "compositor.core"}
                        :compiler {:output-to "resources/public/js/app.js"
                                   :output-dir "resources/public/js/out"
                                   :optimizations :advanced}}]})
