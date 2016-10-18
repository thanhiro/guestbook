(ns guestbook-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :initialised?
  (fn [db _]
    (and (not (empty? db))
         (not (empty? (:visitors-all db))))))

(reg-sub
  :get-greeting
  (fn [db _]
    (:greeting db)))

(reg-sub
  :get-visitors-all
  #(:visitors-all %1))