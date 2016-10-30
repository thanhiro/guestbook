(ns backend.db.core
    (:require [monger.core :as mg]
              [monger.collection :as mc]
              [monger.operators :refer :all]
              [mount.core :refer [defstate]]
              [backend.config :refer [env]]))

(defstate db*
  :start (-> env :database-url mg/connect-via-uri)
  :stop (-> db* :conn mg/disconnect))

(defstate db
  :start (:db db*))

(def visitors-col "visitors")

(defn create-visitor [visitor]
  (mc/insert db visitors-col visitor))

(defn update-visitor [id first-name last-name company host]
  (mc/update db visitors-col {:_id id}
             {$set {:first_name first-name
                    :last_name last-name
                    :company company
                    :host host}}))

(defn get-visitors []
  (mc/find-maps db visitors-col))

(defn get-visitor [id]
  (mc/find-one-as-map db visitors-col {:_id id}))
