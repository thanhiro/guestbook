(ns backend.routes.api
  (:require [backend.layout :as layout]
            [backend.db.core :as db]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [ring.util.http-response :refer :all]
            [clojure.java.io :as io]))

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
    (GET "/visitors" []
      (ok (db/get-visitors)))
    (POST "/visitors" []
      (ok (db/get-visitors)))))

