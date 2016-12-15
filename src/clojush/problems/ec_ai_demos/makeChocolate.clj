;; makeChocolate.clj
;; an example problem for clojush, a Push/PushGP system written in Clojure

(ns clojush.problems.ec-ai-demos.makeChocolate
  (:use [clojush.pushgp.pushgp]
        [clojush.random]
        [clojush pushstate interpreter]
        clojush.instructions.common))

;;;;;;;;;;;;
;; We want make a package of goal kilos of chocolate. We have small bars (1 kilo each) and big bars (5
;; kilos each). Return the number of small bars to use, assuming we always use big bars before small bars.
;; Return -1 if it can't be done.

; makeChocolate(4, 1, 9) → 4
; makeChocolate(4, 1, 10) → -1
; makeChocolate(4, 1, 7) → 2

(def input-set
     [[4 1 9]
      [4 1 7]
      [6 2 7]
      [4 1 5]
      [4 1 4]
      [5 4 9]
      [9 3 18]
      [3 1 9]
      [1 2 7]
      [1 2 6]
      [1 2 5]
      [6 1 10]
      [6 1 11]
      [6 1 12]
      [6 2 10]
      [6 2 11]
      [6 2 12]
      [40 50 260]
      [40 50 265]
      [40 50 275]
      [40 50 280]
      [40 50 285]
      [40 50 290]
      [60 100 550]
      [7 1 12]
      [7 1 13]
      [7 2 13]
      [60 1 65]
      [80 1 75]
      [140 3 16]
      [4 25 128]
      [2 30 150]
      [1 25 126]
      [5 50 50]])


   ;; coding bat expected evaluations

   ;; (4, 1, 9) → 4
   ;; (4, 1, 10) → -1
   ;; (4, 1, 7) → 2
   ;; (6, 2, 7) → 2
   ;; (4, 1, 5) → 0
   ;; (4, 1, 4) → 4
   ;; (5, 4, 9) → 4
   ;; (9, 3, 18) → 3
   ;; (3, 1, 9) → -1
   ;; (1, 2, 7) → -1
   ;; (1, 2, 6) → 1
   ;; (1, 2, 5) → 0
   ;; (6, 1, 10) → 5
   ;; (6, 1, 11) → 6
   ;; (6, 1, 12) → -1
   ;; (6, 1, 13) → -1
   ;; (6, 2, 10) → 0
   ;; (6, 2, 11) → 1
   ;; (6, 2, 12) → 2
   ;; (60, 100, 550) → 50
   ;; (1000, 1000000, 5000006) → 6
   ;; (7, 1, 12) → 7
   ;; (7, 1, 13) → -1
   ;; (7, 2, 13) → 3

; x is small = 1 kilo
; y is big = 5 kilos
; z is goal
; output = how many little bars where used

(defn expected-output
  [inputs]
  (let [[x y z] inputs
    barsLeft (- z (* 5 (min y (quot z 5))))]
    (if (<= barsLeft x) barsLeft
    (if (> barsLeft x) -1))))


(expected-output  [60 1 65])

(expected-output  [80 1 75])

(expected-output  [120 2 110])

(expected-output  [140 3 16])


(expected-output  [4 25 128])

(expected-output  [2 30 150])

(expected-output  [1 25 126])

(expected-output  [5 50 50])





; Make a new push state, and then add every
; input to the special `:input` stack.
; You shouldn't have to change this.
(defn make-start-state
  [inputs]
  (reduce (fn [state input]
            (push-item input :input state))
          (make-push-state)
          inputs))

; The only part of this you'd need to change is
; which stack(s) the return value(s) come from.
(defn actual-output
  [program inputs]
  (let [start-state (make-start-state inputs)
        end-state (run-push program start-state)
        top-int (top-item :integer end-state)]
    top-int))

(defn abs [n]
  (if (< n 0)
    (- n)
    n))

(defn all-errors
  [program]
  (doall
    (for [inputs input-set]
      (let [expected (expected-output inputs)
            actual (actual-output program inputs)]
        (if (= actual :no-stack-item)
          1000
          (abs (- expected actual)))))))


  (def atom-generators
      (concat (registered-for-stacks [:integer :boolean])
            (list 'exec_if)
            (list 5)
            (list (fn [] (lrand-int 1000))
          'in1 'in2 'in3)))

(def argmap
  {:error-function all-errors
   :atom-generators atom-generators
   })
