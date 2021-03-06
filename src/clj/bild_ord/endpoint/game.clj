(ns bild-ord.endpoint.game
  (:require [compojure.core :refer :all]
            [bild-ord.endpoint.common :refer [session-id page title-bar-with-actions]]
            [bild-ord.db :as db]
            [ring.util.response :refer [redirect]]))

(defn game-content []
  "Placeholder element for the Reagent app"
  [:div#app])

(defn game [request]
  (page
   [:div
    (title-bar-with-actions (session-id request))
    (game-content)]
   {:cljs-main "bild_ord.app.main"}))

(defn complete-game [db group request]
  (when-let [username (session-id request)]
    (db/complete-group! db username group))
  (redirect "/"))

(defn game-endpoint [config]
  (routes
   (GET "/game/group/:group/stage/:stage" [_ _]
        game)
   (GET "/game/group/:group/complete" [group]
        (partial complete-game (:db config) group))))
