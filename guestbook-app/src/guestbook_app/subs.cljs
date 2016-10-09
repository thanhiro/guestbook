(ns guestbook-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :initialised?
  (fn  [db _]
    (not (empty? db))))

(reg-sub
  :get-greeting
  (fn [db _]
    (:greeting db)))

(reg-sub
  :get-visitors-today
  #(:visitors-today %1))