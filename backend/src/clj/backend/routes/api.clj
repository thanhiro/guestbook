(ns backend.routes.api
  (:require [backend.layout :as layout]
            [backend.db.core :as db]
            [java-time :as time]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [ring.util.http-response :refer :all]
            [clojure.java.io :as io]))

(s/defschema
  Visitor
  {
   :_id       s/Str
   :firstName s/Str
   :lastName  s/Str
   :company   s/Str
   :host      s/Str
   :date      Long
   :city      s/Str,
   :checkedIn Boolean
   })
(s/defschema NewVisitor (dissoc Visitor :_id))

(defapi api-routes
  {:swagger {:ui "/swagger-ui"
             :spec "/swagger.json"
             :data {:info {:version "1.0.0"
                           :title "Guestbook API"
                           :description "..."}}}}
  (context "/api" []
    (context "/visitors" []
      (GET "/today" []
           :return [Visitor]
           :summary "Today's visitors"
           (ok (db/get-visitors-by-date (time/local-date))))
      (GET "/" []
        :return [Visitor]
        (ok (db/get-visitors)))
      (GET "/:id" req
        :return Visitor
        (ok (db/get-visitor
              (-> req :params :id))))
      (POST "/" req
        :return Visitor
        :body [visitor NewVisitor]
        (ok (db/create-visitor visitor)))
      (PUT "/:id" req
        :return s/Any
        :body [visitor NewVisitor]
        (do (db/update-visitor
              (-> req :params :id) visitor)
            (ok {:status "OK"}))))))

