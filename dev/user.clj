(ns user
  (:require [clojure.repl :refer :all]
            [clojure.pprint :refer [pprint]]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.java.io :as io]
            [com.stuartsierra.component :as component]
            [eftest.runner :as eftest]
            [meta-merge.core :refer [meta-merge]]
            [reloaded.repl :refer [system init start stop go reset]]
            [ring.middleware.stacktrace :refer [wrap-stacktrace]]
            [duct.component.ragtime :as ragtime]
            [duct.component.figwheel :as figwheel]
            [bild-ord.config :as config]
            [bild-ord.system :as system]))

(def dev-config
  {:db  {:connection-uri "jdbc:sqlite:dev.db"}
   :app {:middleware [wrap-stacktrace]}
   :figwheel
   {:css-dirs ["resources/bild_ord/public/css"]
    :builds   [{:source-paths ["src" "dev"]
                :build-options
                {:optimizations   :none
                 :main            "cljs.user"
                 :asset-path      "/js"
                 :output-to       "target/figwheel/bild_ord/public/js/main.js"
                 :output-dir      "target/figwheel/bild_ord/public/js"
                 :source-map      true
                 :source-map-path "/js"}}]}})

(def config
  (meta-merge config/defaults
              config/environ
              dev-config))

(defn new-system []
  (into (system/new-system config)
        {:figwheel (figwheel/server (:figwheel config))}))

(ns-unmap *ns* 'test)

(defn test []
  (eftest/run-tests (eftest/find-tests "test") {:multithread? false}))

(defn cljs-repl []
  (figwheel/cljs-repl (:figwheel system)))

(defn migrate []
  (-> system :ragtime ragtime/reload ragtime/migrate))

(defn rollback
  ([]  (rollback 1))
  ([x] (-> system :ragtime ragtime/reload (ragtime/rollback x))))

(when (io/resource "local.clj")
  (load "local"))

(reloaded.repl/set-init! new-system)
