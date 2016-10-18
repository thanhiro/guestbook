(ns guestbook-app.db
  (:require [clojure.spec :as s]))

;; spec of app-db
(s/def ::greeting string?)
(s/def ::app-db
  (s/keys :req-un [::greeting]))

;; initial state of app-db
(def app-db
  {
   :greeting       "Welcome to Arcusys office"
   :loading        false
   :visitors-all []
   })
