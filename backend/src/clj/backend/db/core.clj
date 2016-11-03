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
  (mc/insert-and-return db visitors-col visitor))

(defn update-visitor [visitor]
  (let [id (:_id visitor)
        v (dissoc visitor :_id)]
       (mc/update db visitors-col {:_id id}
                  {$set v})))

(defn get-visitors []
  (mc/find-maps db visitors-col))

(defn get-visitor [id]
  (mc/find-one-as-map db visitors-col {:_id id}))
