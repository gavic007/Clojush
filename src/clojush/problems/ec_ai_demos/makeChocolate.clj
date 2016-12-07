;; cube_surface.clj
;; an example problem for clojush, a Push/PushGP system written in Clojure
;; Nic McPhee, mcphee@morris.umn.edu, 2016

(ns clojush.problems.ec-ai-demos.cube-surface
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
  [[0 0 0]
   [1 1 1]
   [1 1 2]
   [1 2 1]
   [2 1 1]
   [1 2 2]
   [2 1 2]
   [2 2 1]
   [1 2 3]
   [1 3 2]
   [2 1 3]
   [3 1 2]
   [2 3 1]
   [3 2 1]
   [1 4 5]
   [1 5 4]
   [4 1 5]
   [5 1 4]
   [4 5 1]
   [5 4 1]
   [2 1 8]
   [2 3 4]
   [3 4 2]
   [4 2 3]
   [2 4 3]
   [5 8 9]])


; x is small = 1 kilo
; y is big = 5 kilos
; z is goal
; output = how many little bars where used

(defn expected-output
  [inputs]
  (let [[x y z] inputs
    barsLeft (- z (* 5 (min y (/ z 5))))]
    (if (<= barsLeft x) z)
    (if (> barsLeft x) -1)))





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
  (concat (registered-for-stacks [:integer :boolean :exec])
          (list (fn [] (lrand-int 100))
                'in1 'in2 'in3)))

(def argmap
  {:error-function all-errors
   :atom-generators atom-generators
   })
