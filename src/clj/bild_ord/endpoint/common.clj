(ns bild-ord.endpoint.common
  (:require [hiccup.page :refer [html5 include-js include-css]]))

(defn page
  "Base page layout"
  ([body] (page body {}))
  ([body options]
   (html5
    {:lang "sv"}
    [:head
     [:title "Bild och ord"]
     (include-css "/css/base.css")
     (include-css "/css/main.css")]
    [:body
     (when-let [class (:class options)]
       {:class class})
     body
     (include-js "/js/main.js")
     (when (:cljs-main options)
       [:script (str (:cljs-main options) "();")])])))

(defn title-bar
  "Top title bar shown on some pages"
  []
  [:nav.clearfix.title-bar
   [:div.sm-col
    [:h1.m0 "Bild och ord"]]])
