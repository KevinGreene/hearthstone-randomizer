(ns hearthstone-randomizer.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [cljsjs.react :as react]
              [hearthstone-randomizer.hearthstone :as hs])
    (:import goog.History))

(def player1 (atom ""))
(def player2 (atom ""))
(def match (atom  {:characters ["Druid" "Hunter"] :style "Class Specific"}))

;; -------------------------
;; Views

(defn home-page []
  [:div.container 
   [:div.app [:h2 "Welcome!"]
    (player-names-component player1 player2)
    (refresh-button-component match)
    (match-component player1 player2 match)
    [:div [:a {:href "#"} "go to about page"]]]])

(defn about-page []
  [:div [:h2 "About hearthstone-randomizer"]
   [:div [:a {:href "#/"} "go to the home page"]]])

(defn current-page []
  [:div [(session/get :current-page)]])

(defn player-names-component [player1 player2]
  [:div.player-names
   [:form.form-inline
    (player-name-component player1 "Player 1")
    (player-name-component player2 "Player 2")]])

(defn player-name-component [player label]
  [:div.input-group
   [:label label]
   [:input.form-control
    {:type "text"
     :value @player
     :on-change #(reset! player (-> % .-target .-value))}]]  )

(defn refresh-match [match]
  (reset! match (hs/get-match)))

(defn refresh-button-component [match]
  [:button
   {:type "submit"
    :class "btn btn-default"
    :on-click #(refresh-match match)} "Refresh"])

(defn match-component [player1 player2 match]
  (let [{:keys [characters style]} @match]
    [:div.match
     (format-player-component @player1 (nth characters 0))
     (format-player-component @player2 (nth characters 1))
     (format-style-component style)]))

(defn format-player-component [player-name character-name]
  [:p (str player-name " is " character-name)])

(defn format-style-component [style]
  [:p (str "The style is " style)])

;; -------------------------
;; Routes
(secretary/set-config!)
(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/about" []
  (session/put! :current-page #'about-page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn init! []
  (hook-browser-navigation!)
  (reagent/render-component [current-page] (.getElementById js/document "app")))
