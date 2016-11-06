(ns guestbook-app.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :initialised?
  (fn [db _]
    (and (not (empty? db))
         (not (empty? (:visitors-today db))))))

(reg-sub
  :get-loading
  (fn [db _]
    (:loading? db)))

(reg-sub
  :get-error
  (fn [db _]
    (:error db)))

(reg-sub
  :get-greeting
  (fn [db _]
    (:greeting db)))

(reg-sub
  :get-visitors-today
  #(:visitors-today %1))

(reg-sub
  :get-visitors-all
  #(:visitors-all %1))