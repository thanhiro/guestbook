(ns backend.db.core
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer :all]
            [mount.core :refer [defstate]]
            [backend.config :refer [env]])
  (:import org.bson.types.ObjectId))

(defstate db*
  :start (-> env :database-url mg/connect-via-uri)
  :stop (-> db* :conn mg/disconnect))

(defstate db
  :start (:db db*))

(def visitors-col "visitors")

(defn serialize-object-id-in [map]
  (assoc map :_id (str (:_id map))))

(defn create-visitor [visitor]
  (serialize-object-id-in
    (mc/insert-and-return db visitors-col visitor)))

(defn update-visitor [id visitor]
  (let [v (dissoc visitor :_id)]
    (println v)
    (mc/update db visitors-col {:_id (ObjectId. id)}
               {$set v})))

(defn get-visitors-by-date [date]
  (let
    [res (mc/find-maps db visitors-col)]
    (map serialize-object-id-in res)))

(defn get-visitors []
  (let
    [res (mc/find-maps db visitors-col)]
    (map serialize-object-id-in res)))

(defn get-visitor [id]
  (let
    [res
     (mc/find-one-as-map db visitors-col
                         {:_id (ObjectId. id)})]
    (serialize-object-id-in res)))
