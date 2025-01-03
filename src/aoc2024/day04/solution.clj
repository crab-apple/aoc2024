(ns aoc2024.day04.solution
  (:require [aoc2024.grid :as gr]
            [aoc2024.input :as in]
            [aoc2024.utils :as utils]
            [clojure.string :as str]))

(defn- slice-across-rows-and-cols [grid]
  (let [rotated-grid (apply map vector grid)]
    (flatten [(map str/join grid)
              (map str/join (map reverse grid))
              (map str/join rotated-grid)
              (map str/join (map reverse rotated-grid))])))

(defn- slice-diagonally-nw-se [grid]
  (let [origin [[0 0]]
        starts-top (map #(vector 0 %1) (range 1 (gr/width grid)))
        starts-side (map #(vector %1 0) (range 1 (gr/height grid)))
        starts (concat origin starts-top starts-side)
        diagonals (map (fn [start] (gr/diagonal-nw-se grid start)) starts)]
    (map str/join diagonals)))

(defn soup-slice [soup]
  (let [grid (in/as-grid soup)]
    (concat
     (slice-across-rows-and-cols grid)
     (slice-diagonally-nw-se grid)
     (slice-diagonally-nw-se (gr/rotate-clockwise grid))
     (slice-diagonally-nw-se (gr/rotate-clockwise (gr/rotate-clockwise grid)))
     (slice-diagonally-nw-se (gr/rotate-clockwise (gr/rotate-clockwise (gr/rotate-clockwise grid)))))))

(defn count-substring-occurrences [needle haystack]
  (let [index (str/index-of haystack needle)]
    (if index (inc (count-substring-occurrences needle (subs haystack (inc index)))) 0)))

(defn solution-1 [soup]
  (let [count-xmas (partial count-substring-occurrences "XMAS")
        slices (soup-slice soup)]
    (apply + (map count-xmas slices))))

(defn- x-mas? [grid point]
  (and
   (= (gr/grid-get grid (map + point)) \A)
   (or
    (and
     (= (gr/grid-get grid (map + point [1 1])) \M)
     (= (gr/grid-get grid (map + point [-1 -1])) \S))
    (and
     (= (gr/grid-get grid (map + point [1 1])) \S)
     (= (gr/grid-get grid (map + point [-1 -1])) \M)))
   (or
    (and
     (= (gr/grid-get grid (map + point [-1 1])) \M)
     (= (gr/grid-get grid (map + point [1 -1])) \S))
    (and
     (= (gr/grid-get grid (map + point [-1 1])) \S)
     (= (gr/grid-get grid (map + point [1 -1])) \M)))))

(defn solution-2 [soup]
  (let [grid (in/as-grid soup)
        points (utils/cartesian (range 0 (gr/height grid)) (range 0 (gr/width grid)))]
    (count (filter (partial x-mas? grid) points))))
