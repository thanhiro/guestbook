(ns backend.routes.api
  (:require [backend.layout :as layout]
            [backend.db.core :as db]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [ring.util.http-response :refer :all]
            [clojure.java.io :as io])
    (:import org.bson.types.ObjectId))

(s/defschema
  Visitor
  {
   :_id        ObjectId
   :first-name s/Str
   :last-name  s/Str
   :company    s/Str
   :host       s/Str
   :date       Long
   :city       s/Str
   })
(s/defschema NewVisitor (dissoc Visitor :_id))

(defapi api-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Guestbook API"
                           :description "..."}}}}
  (context "/api" []
    (GET "/visitors/today" []
      :summary "Today's visitors"
      (ok (db/get-visitors)))
    (context "/visitors"
      (GET []
        (ok (db/get-visitors)))
      (POST req
        :return s/Any
        :body [visitor NewVisitor]
        (ok {:_id
             (.toString
               (:_id (db/create-visitor visitor)))}))
      (PUT req
        :return s/Any
        :body [visitor NewVisitor]
        (ok {:_id
          (.toString
            (:_id (db/update-visitor visitor)))})))))
