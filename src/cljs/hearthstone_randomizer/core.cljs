(ns hearthstone-randomizer.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [cljsjs.react :as react]
              [hearthstone-randomizer.hearthstone :as hs])
    (:import goog.History))

(def player1 (atom "Player 1"))
(def player2 (atom "Player 2"))
(def match (atom  {:characters ["Druid" "Hunter"] :style "Class Specific"}))

;; -------------------------
;; Views

(defn home-page []
  (let [{:keys [characters style]} @match
        player1-hero (nth characters 0)
        player2-hero (nth characters 1)]
    [:div.container
     [:div.app [:h2.app-title "Hearthstone Randomizer"]
      [:div.col-md-3
       (player-name-component player1 player1-hero)]
      [:div.col-md-6
       (refresh-button-component match)
       (match-component player1 player2 match)]
      [:div.col-md-3
       (player-name-component player2 player2-hero)
       ]]]))

(defn current-page []
  [:div [(session/get :current-page)]])

(defn player-names-component [player1 player2]
  [:div.player-names
   [:form.form-inline
    (player-name-component player1)
    (player-name-component player2)]])

(defn player-name-component [player hero]
  [:div.input-group
   [:input.form-control
    {:type "text"
     :class "player-name-input"
     :value @player
     :on-change #(reset! player (-> % .-target .-value))}]
   [:img.hero-image {:src (str "images/" hero ".png")}]]  )

(defn refresh-match [match]
  (reset! match (hs/get-match)))

(defn refresh-button-component [match]
  [:button
   {:type "submit"
    :class "btn btn-default refresh-button"
    :on-click #(refresh-match match)} "Refresh"])

(defn match-component [player1 player2 match]
  (let [{:keys [characters style]} @match]
    [:div.match
     (format-player-component @player1 (nth characters 0))
     (format-player-component @player2 (nth characters 1))
     (format-style-component style)
     (format-description style)]))

(defn format-player-component [player-name character-name]
  [:p (str player-name " is " character-name)])

(defn format-style-component [style]
  [:p (str "The style is " (:name style))])

(defn format-description [style]
  [:div {:class "col-md-8 col-md-offset-2"}
   [:div.style-description
    [:h3 "How to Play"]
    [:p (str (:desc style))]]])

;; -------------------------
;; Routes
(secretary/set-config!)
(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

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
  (reagent/render-component [current-page] (.getElementById js/document "app"))
  (refresh-match match))
