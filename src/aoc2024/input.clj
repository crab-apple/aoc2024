(ns aoc2024.input
  (:require [clojure.string :as str]))

(defn lines [in]
  (mapv str/trim (str/split-lines (str/trim in))))

(defn numstr? [in]
  (not (nil? (re-matches #"\d+" in))))

(defn token [in]
  (if (numstr? in) (parse-long in) in))

(defn tokens [in]
  (mapv token (str/split (str/trim in) #"\s+")))

(defn parse-input [in]
  (mapv tokens (lines in)))

(defn as-grid [in]
  (mapv vec (lines in)))
