(ns bild-ord.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [register-sub]]
            [bild-ord.domain.game :as game]))

(register-sub
 :slots
 (fn [db _]
   (reaction (-> @db :games (get (:stage @db)) ::game/slots))))

;; Keeps track of the words available to try.
;; Only for the dragging stage of a game.
(register-sub
 :pile
 (fn [db _]
   (reaction (-> @db :games :drag ::game/pile))))

(register-sub
 :current-group
 (fn [db _]
   (reaction (:group @db))))

(register-sub
 :current-stage
 (fn [db _]
   (reaction (:stage @db))))

(register-sub
 :success?
 (fn [db _]
   (reaction (some-> @db :games (get (:stage @db)) game/success?))))

;; Progress from 0 to 100 of the entire game
(register-sub
 :progress
 (fn [db _]
   (reaction
    (* (+ (game/progress (-> @db :games :drag))
          (game/progress (-> @db :games :type)))
       10))))
