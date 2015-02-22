(ns hearthstone-randomizer.hearthstone)

(def characters ["Druid" "Hunter" "Mage" "Paladin" "Priest" "Rogue" "Shaman" "Warlock" "Warrior"])

(def styles ["Suggest a Card" "Opponent's Choice" "Random - Click Done" "Max 3 Cost" "Minimum 5 Cost" "Class Specific" "Neutral Only"])

(defn format-characters [kevins rachels]
  (println (str "Kevin's is " kevins))
  (println (str "Rachel's is " rachels)))

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
