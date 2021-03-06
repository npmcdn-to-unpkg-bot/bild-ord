(ns bild-ord.endpoint.common
  (:require [clojure.java.io :as io]
            [clojure.string :as st]
            [hiccup.page :refer [html5 include-js include-css]]))

(defn current-version []
  (try
    (-> "cache_version" io/resource slurp st/trim-newline)
    (catch java.lang.IllegalArgumentException e
      (println "Couldn't read from /resource/cache_version file")
      "any")))

(defn versioned-resource [path]
  (str path "?_version=" (current-version)))

(defn page
  "Base page layout"
  ([body] (page body {}))
  ([body options]
   (html5
    {:lang "sv"}
    [:head
     [:title "Bild och ord"]
     [:link {:rel "apple-touch-icon-precomposed" :href "/favicon-152.png"}]
     (include-css "/css/base.css")
     (include-css
      (versioned-resource "/css/main.css"))]
    [:body
     (when-let [class (:class options)]
       {:class class})
     body
     (include-js
      (versioned-resource "/js/main.js"))
     (when (:cljs-main options)
       [:script (str (:cljs-main options) "();")])])))

(defn title-bar
  "Top title bar"
  []
  [:nav.clearfix.title-bar
   [:div.col.col-1]
   [:div.col.col-11
    [:a.h1 {:href "/"} "Bild och ord"]]])

(defn title-bar-with-actions
  "Title bar that includes actions, such as login/logout"
  [id]
  [:nav.clearfix.title-bar
   [:div.col.col-1]
   [:div.col.col-9
    [:a.h1 {:href "/"} "Bild och ord"]]
   [:div.col.col-2
    (if id
      [:div.actions.menu-header {:id "menu-header"} id
       [:div.absolute.menu
        [:a {:href "/logout" } "Logga ut"]]]
      [:div.actions
       [:a {:href "/login"} "Logga in"]])]])

(defn session-id
  [request]
  (-> request :session :identity))

(defn set-session-id
  [request id]
  (assoc-in request [:session :identity] id))

(defn clear-session-id
  [request]
  (assoc-in request [:session :identity] nil))
