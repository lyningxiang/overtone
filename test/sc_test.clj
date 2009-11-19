(ns sc-test
  (:use 
     clojure.test
     clojure.contrib.seq-utils
     clj-backtrace.repl
     overtone.utils)
  (:require 
     [overtone.sc :as sc]
     [overtone.log :as log]))

(def ditty-notes [50 50 57 50 48 62 62 50])
(def ditty-durs  [250 250 500 125 125 250 250 500])

(defn play [inst notes durs]
  (loop [notes notes
         durs durs
         t (now)]
    (when (and notes durs)
      (sc/hit t inst :pitch (first notes) :dur (first durs))
      (recur (next notes) (next durs) (+ t (first durs))))))

(deftest boot-test []
  (try 
    (sc/boot)
    (is (not (nil? @sc/server*)))
    (is (= 1 (:n-groups (sc/status))))
    (play "sin" ditty-notes ditty-durs)
    (Thread/sleep 3000)
    (finally 
      (sc/quit))))

(defn groups-test []
  (sc/group :head sc/DEFAULT-GROUP)
  (sc/group :head sc/DEFAULT-GROUP)
  (sc/group :head sc/DEFAULT-GROUP)
  (is (= 4 (:n-groups (sc/status)))))

(defn node-tree-test []
  (sc/reset)
  (sc/group :head 0)
  (sc/group :tail 0)
  (sc/hit :sin :dur 10000 :target 2)
  (is (= 1 (:n-synths (sc/status))))
  (

; These are what the responses look like for a queryTree msg.  The first
; without and the second with control information.
(def no-ctls [0 0 2 1 2 2 0 3 0 1001 -1 "sin"])
(def with-ctls [1 0 2 1 2 2 0 3 0 1001 -1 "sin" 3 "out" 0.0 "pitch" 40.0 "dur" 100000.0])

(deftest server-messaging-test []
  (try
    (sc/boot)
    (groups-test)
    (nodes-test)
    (finally 
      (sc/quit))))


