(ns hearthstone-randomizer.hearthstone)

(def characters
  ["Druid"
   "Hunter"
   "Mage"
   "Paladin"
   "Priest"
   "Rogue"
   "Shaman"
   "Warlock"
   "Warrior"])

(def styles
  [{:name "Suggest a Card"
    :desc "Decks are made by starting a new deck, clicking suggest a card, and picking from the cards available until a deck is made"}
   {:name "Opponent's Choice"
    :desc "Decks must by made by your opponent, who follows the Suggest-A-Card rules."}
   {:name "Random"
    :desc "Decks must by made by starting a new deck and simply clicking done"}
   {:name "Max 3 Cost"
    :desc "Decks must only contain cards of at most 3 cost"}
   {:name "Minimum 5 Cost"
    :desc "Decks must only contain cards of at least 5 cost. You cannot use your hero power until a non-coin card has been played"}
   {:name "Class Specific"
    :desc "Decks must only use class specific cards"}
   {:name "Neutral Only"
    :desc "Decks cannot use any class specific cards"}
   {:name "Pauper"
    :desc "Decks cannot use cards above Common in rarity. All cards must have a clear gem, or no gem"}
   {:name "Don't Be Basic"
    :desc "Decks cannot use basic cards, i.e. cards without a gem in the center"}
   {:name "Cool Cards"
    :desc "Decks must only use minions with types, e.g. Mech, Demon, etc. No restriction is placed on spells"}
   {:name "Rejects"
    :desc "Decks cannot use any cards with types, e.g. Mech, Demon, etc."}])

(defn format-characters [player1 player2]
  (println (str "Player 1 is " player1))
  (println (str "Player 2 is" player2)))

(defn get-characters []
  (take 2 (shuffle characters)))

(defn format-style [style]
  (println (str "The style is " style)))

(defn get-style []
  (first (shuffle styles)))

(defn format-match [match]
  (let [{:keys [characters style]} match]
    (apply format-characters characters)
    (apply format-style style)))

(defn get-match []
  {:characters (get-characters)
   :style (get-style)})
