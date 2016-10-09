(ns guestbook-app.db
  (:require [clojure.spec :as s]))

;; spec of app-db
(s/def ::greeting string?)
(s/def ::app-db
  (s/keys :req-un [::greeting]))

;; initial state of app-db
(def app-db
  {
   :greeting       "Welcome to Arcusys Oulu office"
   :loading        false
   :visitors-today (clj->js
                     [
                      {:firstName "Teppo" :lastName "Winnipegin" :company "Jets" :host "Spede Pasanen"}
                      {:firstName "Reijo" :lastName "Ruotsalainen" :company "Oilers" :host "Simo Salminen"}
                      {:firstName "Vellu" :lastName "Ketola" :company "Ässät" :host "Vesa-Matti Loiri"}
                      ])
   })
